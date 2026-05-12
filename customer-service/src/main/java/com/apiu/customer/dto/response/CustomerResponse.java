package com.apiu.customer.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

// Lo que el API devuelve al cliente. Incluimos userId para que el frontend
// pueda asociar el perfil con el usuario autenticado.
@Data
@Builder
public class CustomerResponse {

    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
