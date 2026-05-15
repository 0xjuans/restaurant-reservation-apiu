package com.apiu.reservation.dto.request;

import com.apiu.reservation.entity.ReservationStatus;
import jakarta.validation.constraints.NotNull;

public record ReservationStatusRequest(
        @NotNull ReservationStatus status
) {}
