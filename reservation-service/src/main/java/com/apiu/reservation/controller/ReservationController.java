package com.apiu.reservation.controller;

import com.apiu.reservation.dto.request.ReservationRequest;
import com.apiu.reservation.dto.request.ReservationStatusRequest;
import com.apiu.reservation.dto.response.ReservationResponse;
import com.apiu.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Crear, consultar, actualizar y cancelar reservas")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Listar todas las reservas")
    public List<ReservationResponse> getAll(@Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        return reservationService.getAll(token);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID")
    public ReservationResponse getById(@PathVariable Long id,
                                       @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        return reservationService.getById(id, token);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Listar reservas de un cliente")
    public List<ReservationResponse> getByCustomer(@PathVariable Long customerId,
                                                   @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        return reservationService.getByCustomer(customerId, token);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear una reserva", description = "Valida disponibilidad de mesa y horario antes de confirmar")
    public ReservationResponse create(@Valid @RequestBody ReservationRequest request,
                                      @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        return reservationService.create(request, token);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Actualizar estado de la reserva", description = "Estados válidos: PENDING, CONFIRMED, CANCELLED, COMPLETED")
    public ReservationResponse updateStatus(@PathVariable Long id,
                                            @Valid @RequestBody ReservationStatusRequest request,
                                            @Parameter(hidden = true) @RequestHeader("Authorization") String token) {
        return reservationService.updateStatus(id, request, token);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar una reserva")
    public void delete(@PathVariable Long id) {
        reservationService.delete(id);
    }
}
