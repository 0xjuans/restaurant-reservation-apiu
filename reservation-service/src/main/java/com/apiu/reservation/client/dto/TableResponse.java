package com.apiu.reservation.client.dto;

// Subconjunto del TableResponse del table-service que necesitamos aquí
public record TableResponse(
        Long id,
        Long zoneId,
        String zoneName,
        Integer number,
        Integer capacity,
        String status
) {}
