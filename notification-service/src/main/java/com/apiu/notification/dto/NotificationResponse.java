package com.apiu.notification.dto;

import com.apiu.notification.entity.NotificationStatus;
import com.apiu.notification.entity.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String recipientEmail,
        NotificationType type,
        Long reservationId,
        NotificationStatus status,
        String errorMessage,
        LocalDateTime sentAt
) {}
