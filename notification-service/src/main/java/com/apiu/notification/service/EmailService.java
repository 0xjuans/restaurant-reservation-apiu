package com.apiu.notification.service;

import com.apiu.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void send(NotificationRequest req) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(req.recipientEmail());
        message.setSubject(buildSubject(req));
        message.setText(buildBody(req));

        mailSender.send(message);
        log.debug("Email enviado a {} — tipo: {}", req.recipientEmail(), req.type());
    }

    private String buildSubject(NotificationRequest req) {
        return switch (req.type()) {
            case RESERVATION_CREATED   -> "Reserva recibida — Mesa #" + req.tableNumber();
            case RESERVATION_CONFIRMED -> "Reserva confirmada — Mesa #" + req.tableNumber();
            case RESERVATION_CANCELLED -> "Reserva cancelada";
            case RESERVATION_COMPLETED -> "Gracias por tu visita";
        };
    }

    private String buildBody(NotificationRequest req) {
        return switch (req.type()) {
            case RESERVATION_CREATED -> """
                    Hola %s,

                    Tu reserva fue recibida y está PENDIENTE de confirmación.

                    Detalles:
                      Mesa: #%d (%s)
                      Fecha: %s
                      Horario: %s - %s

                    Te avisaremos cuando sea confirmada.
                    """.formatted(req.customerName(), req.tableNumber(), req.zoneName(),
                    req.date(), req.startTime(), req.endTime());

            case RESERVATION_CONFIRMED -> """
                    Hola %s,

                    Tu reserva fue CONFIRMADA.

                    Detalles:
                      Mesa: #%d (%s)
                      Fecha: %s
                      Horario: %s - %s

                    ¡Te esperamos!
                    """.formatted(req.customerName(), req.tableNumber(), req.zoneName(),
                    req.date(), req.startTime(), req.endTime());

            case RESERVATION_CANCELLED -> """
                    Hola %s,

                    Tu reserva del %s ha sido CANCELADA.

                    Si tienes dudas, contáctanos.
                    """.formatted(req.customerName(), req.date());

            case RESERVATION_COMPLETED -> """
                    Hola %s,

                    ¡Gracias por visitarnos el %s!
                    Esperamos que hayas disfrutado tu experiencia.
                    """.formatted(req.customerName(), req.date());
        };
    }
}
