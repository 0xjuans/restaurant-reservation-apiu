package com.apiu.customer.controller;

import com.apiu.customer.dto.request.CustomerRequest;
import com.apiu.customer.dto.response.CustomerResponse;
import com.apiu.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión del perfil de cliente")
@SecurityRequirement(name = "bearerAuth") // indica a Swagger que estos endpoints requieren JWT
public class CustomerController {

    private final CustomerService customerService;

    // Endpoint interno: usado por reservation-service vía Feign para enriquecer reservas
    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getById(id));
    }

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear perfil de cliente")
    public ResponseEntity<CustomerResponse> createProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CustomerRequest request) {

        // @AuthenticationPrincipal inyecta el usuario autenticado que Spring Security
        // guardó en el SecurityContext al validar el JWT.
        // El username que guardamos en el token es el userId como String.
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.createProfile(userId, request));
    }

    @GetMapping("/profile")
    @Operation(summary = "Obtener perfil del cliente autenticado")
    public ResponseEntity<CustomerResponse> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(customerService.getProfile(userId));
    }

    @PutMapping("/profile")
    @Operation(summary = "Actualizar perfil del cliente autenticado")
    public ResponseEntity<CustomerResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CustomerRequest request) {

        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(customerService.updateProfile(userId, request));
    }
}
