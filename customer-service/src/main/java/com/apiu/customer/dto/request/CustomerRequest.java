package com.apiu.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

// Lo que el cliente envía en el body del request.
// Las anotaciones de validación rechazan datos inválidos antes de llegar al servicio.
@Data
public class CustomerRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String lastName;

    // Teléfono opcional. El patrón acepta formatos como +57 300 123 4567
    @Pattern(regexp = "^[+]?[0-9\\s\\-]{7,20}$", message = "Formato de teléfono inválido")
    private String phone;
}
