package com.apiu.notification.dto;

import com.apiu.notification.entity.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record NotificationRequest(

        @NotNull Long reservationId,
        @NotBlank @Email String recipientEmail,
        @NotBlank String customerName,
        @NotNull NotificationType type,

        // Datos de la reserva para armar el cuerpo del email
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Integer tableNumber,
        String zoneName
) {}
