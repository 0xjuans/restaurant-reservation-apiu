package com.apiu.reservation.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequest(

        @NotNull Long customerId,
        @NotNull Long tableId,

        @NotNull @Future LocalDate date,

        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,

        @NotNull @Min(1) Integer guestsCount,

        String notes
) {}
