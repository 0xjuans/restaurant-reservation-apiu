package com.apiu.reservation.client.dto;

// Subconjunto del CustomerResponse del customer-service que necesitamos aquí
public record CustomerResponse(
        Long id,
        Long userId,
        String firstName,
        String lastName,
        String phone
) {}
