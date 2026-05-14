package com.apiu.table.dto.response;

import com.apiu.table.entity.TableStatus;

import java.time.LocalDateTime;

public record TableResponse(
        Long id,
        Long zoneId,
        String zoneName,
        Integer number,
        Integer capacity,
        TableStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
