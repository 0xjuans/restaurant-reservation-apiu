package com.apiu.table.dto.request;

import com.apiu.table.entity.TableStatus;
import jakarta.validation.constraints.NotNull;

public record TableStatusRequest(

        @NotNull(message = "El estado es obligatorio")
        TableStatus status
) {}
