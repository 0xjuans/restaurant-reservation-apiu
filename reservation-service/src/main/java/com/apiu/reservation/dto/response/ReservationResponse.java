package com.apiu.reservation.dto.response;

import com.apiu.reservation.entity.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReservationResponse(
        Long id,
        Long customerId,
        String customerName,
        Long tableId,
        String tableZoneName,
        Integer tableNumber,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Integer guestsCount,
        ReservationStatus status,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
