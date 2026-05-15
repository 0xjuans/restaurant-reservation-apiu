package com.apiu.reservation.client.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record NotificationRequest(
        Long reservationId,
        String recipientEmail,
        String customerName,
        String type,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Integer tableNumber,
        String zoneName
) {}
