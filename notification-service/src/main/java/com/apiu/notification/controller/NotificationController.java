package com.apiu.notification.controller;

import com.apiu.notification.dto.NotificationRequest;
import com.apiu.notification.dto.NotificationResponse;
import com.apiu.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Envío y consulta de notificaciones por email")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Enviar notificación", description = "Envía un email y registra el resultado")
    public NotificationResponse send(@Valid @RequestBody NotificationRequest request) {
        return notificationService.send(request);
    }

    @GetMapping("/reservation/{reservationId}")
    @Operation(summary = "Historial de notificaciones de una reserva")
    public List<NotificationResponse> getByReservation(@PathVariable Long reservationId) {
        return notificationService.getByReservation(reservationId);
    }
}
