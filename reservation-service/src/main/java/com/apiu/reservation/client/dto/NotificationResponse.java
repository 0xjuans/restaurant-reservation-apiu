package com.apiu.reservation.client.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String recipientEmail,
        String type,
        Long reservationId,
        String status,
        String errorMessage,
        LocalDateTime sentAt
) {}
