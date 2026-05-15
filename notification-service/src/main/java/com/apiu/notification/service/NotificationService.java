package com.apiu.notification.service;

import com.apiu.notification.dto.NotificationRequest;
import com.apiu.notification.dto.NotificationResponse;
import com.apiu.notification.entity.NotificationLog;
import com.apiu.notification.entity.NotificationStatus;
import com.apiu.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationLogRepository logRepository;
    private final EmailService emailService;

    // Intenta enviar el email y registra el resultado (SENT o FAILED)
    public NotificationResponse send(NotificationRequest req) {
        NotificationLog notifLog = NotificationLog.builder()
                .reservationId(req.reservationId())
                .recipientEmail(req.recipientEmail())
                .type(req.type())
                .status(NotificationStatus.SENT)
                .build();

        try {
            emailService.send(req);
        } catch (Exception e) {
            log.warn("No se pudo enviar email a {}: {}", req.recipientEmail(), e.getMessage());
            notifLog.setStatus(NotificationStatus.FAILED);
            notifLog.setErrorMessage(e.getMessage().length() > 500
                    ? e.getMessage().substring(0, 500) : e.getMessage());
        }

        return toResponse(logRepository.save(notifLog));
    }

    public List<NotificationResponse> getByReservation(Long reservationId) {
        return logRepository.findByReservationIdOrderBySentAtDesc(reservationId)
                .stream().map(this::toResponse).toList();
    }

    private NotificationResponse toResponse(NotificationLog log) {
        return new NotificationResponse(
                log.getId(),
                log.getRecipientEmail(),
                log.getType(),
                log.getReservationId(),
                log.getStatus(),
                log.getErrorMessage(),
                log.getSentAt()
        );
    }
}
