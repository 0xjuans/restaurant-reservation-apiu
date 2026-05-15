package com.apiu.reservation.service;

import com.apiu.reservation.client.CustomerClient;
import com.apiu.reservation.client.NotificationClient;
import com.apiu.reservation.client.TableClient;
import com.apiu.reservation.client.dto.CustomerResponse;
import com.apiu.reservation.client.dto.NotificationRequest;
import com.apiu.reservation.client.dto.TableResponse;
import com.apiu.reservation.dto.request.ReservationRequest;
import com.apiu.reservation.dto.request.ReservationStatusRequest;
import com.apiu.reservation.dto.response.ReservationResponse;
import com.apiu.reservation.entity.Reservation;
import com.apiu.reservation.entity.ReservationStatus;
import com.apiu.reservation.repository.ReservationRepository;
import com.apiu.reservation.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerClient customerClient;
    private final TableClient tableClient;
    private final NotificationClient notificationClient;
    private final JwtService jwtService;

    public List<ReservationResponse> getAll(String token) {
        return reservationRepository.findAll().stream()
                .map(r -> toResponse(r, token))
                .toList();
    }

    public List<ReservationResponse> getByCustomer(Long customerId, String token) {
        return reservationRepository.findByCustomerId(customerId).stream()
                .map(r -> toResponse(r, token))
                .toList();
    }

    public ReservationResponse getById(Long id, String token) {
        return toResponse(findOrThrow(id), token);
    }

    public ReservationResponse create(ReservationRequest req, String token) {
        // Valida que el cliente y la mesa existen llamando a sus servicios
        CustomerResponse customer = customerClient.getById(token, req.customerId());
        TableResponse table = tableClient.getById(token, req.tableId());

        // La mesa no puede estar fuera de servicio
        if ("MAINTENANCE".equals(table.status())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La mesa " + table.number() + " está en mantenimiento");
        }

        // Los comensales no pueden superar la capacidad de la mesa
        if (req.guestsCount() > table.capacity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La mesa tiene capacidad para " + table.capacity() + " personas");
        }

        // Detecta solapamiento con reservas existentes en la misma mesa y fecha
        boolean conflict = reservationRepository.existsConflict(
                req.tableId(), req.date(), req.startTime(), req.endTime(), -1L);
        if (conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La mesa ya tiene una reserva en ese horario");
        }

        Reservation saved = reservationRepository.save(Reservation.builder()
                .customerId(req.customerId())
                .tableId(req.tableId())
                .date(req.date())
                .startTime(req.startTime())
                .endTime(req.endTime())
                .guestsCount(req.guestsCount())
                .notes(req.notes())
                .build());

        // Notifica al cliente por email de forma no bloqueante (si falla, solo se loguea)
        sendNotification(saved, customer, table, "RESERVATION_CREATED", token);

        return buildResponse(saved, customer, table);
    }

    public ReservationResponse updateStatus(Long id, ReservationStatusRequest req, String token) {
        Reservation reservation = findOrThrow(id);
        reservation.setStatus(req.status());
        Reservation saved = reservationRepository.save(reservation);

        // Notifica al cliente cuando el estado cambia
        String notifType = switch (req.status()) {
            case CONFIRMED  -> "RESERVATION_CONFIRMED";
            case CANCELLED  -> "RESERVATION_CANCELLED";
            case COMPLETED  -> "RESERVATION_COMPLETED";
            default         -> null;
        };
        if (notifType != null) {
            CustomerResponse customer = customerClient.getById(token, saved.getCustomerId());
            TableResponse table = tableClient.getById(token, saved.getTableId());
            sendNotification(saved, customer, table, notifType, token);
        }

        return toResponse(saved, token);
    }

    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada: " + id);
        }
        reservationRepository.deleteById(id);
    }

    // Construye el response enriquecido llamando a los otros servicios
    private ReservationResponse toResponse(Reservation r, String token) {
        CustomerResponse customer = customerClient.getById(token, r.getCustomerId());
        TableResponse table = tableClient.getById(token, r.getTableId());
        return buildResponse(r, customer, table);
    }

    private ReservationResponse buildResponse(Reservation r, CustomerResponse c, TableResponse t) {
        return new ReservationResponse(
                r.getId(),
                c.id(),
                c.firstName() + " " + c.lastName(),
                t.id(),
                t.zoneName(),
                t.number(),
                r.getDate(),
                r.getStartTime(),
                r.getEndTime(),
                r.getGuestsCount(),
                r.getStatus(),
                r.getNotes(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }

    private Reservation findOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reserva no encontrada: " + id));
    }

    // Envía notificación al notification-service; si falla no interrumpe el flujo principal
    private void sendNotification(Reservation r, CustomerResponse c, TableResponse t,
                                  String type, String token) {
        try {
            String email = jwtService.extractEmail(token.replace("Bearer ", ""));
            NotificationRequest notifReq = new NotificationRequest(
                    r.getId(),
                    email,
                    c.firstName() + " " + c.lastName(),
                    type,
                    r.getDate(),
                    r.getStartTime(),
                    r.getEndTime(),
                    t.number(),
                    t.zoneName()
            );
            notificationClient.send(token, notifReq);
        } catch (Exception e) {
            log.warn("No se pudo enviar notificación para reserva {}: {}", r.getId(), e.getMessage());
        }
    }
}
