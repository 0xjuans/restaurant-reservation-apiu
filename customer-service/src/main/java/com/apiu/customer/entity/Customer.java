package com.apiu.customer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// @Entity le dice a JPA que esta clase es una tabla en la base de datos.
// Cada objeto Customer que crees en Java se puede guardar como una fila en la tabla.
@Entity
// @Table especifica el nombre de la tabla y el esquema donde vive.
// Sin esto, JPA buscaría una tabla llamada "customer" en el esquema por defecto.
@Table(name = "customers", schema = "customers")
// Lombok: genera automáticamente getters, setters, equals, hashCode y toString
@Data
// Lombok: genera un patrón Builder → Customer.builder().firstName("Juan").build()
@Builder
// Lombok: genera constructor vacío (requerido por JPA para instanciar entidades)
@NoArgsConstructor
// Lombok: genera constructor con todos los campos
@AllArgsConstructor
public class Customer {

    // @Id marca el campo como clave primaria de la tabla
    @Id
    // @GeneratedValue le dice a JPA que la base de datos genera el ID automáticamente
    // IDENTITY usa el BIGSERIAL de PostgreSQL (autoincremental)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del usuario en el auth-service. UNIQUE garantiza que un usuario
    // solo pueda tener un perfil de cliente. No es una FK real porque
    // en microservicios los servicios no comparten base de datos.
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    // nullable = true por defecto, el teléfono es opcional
    @Column(length = 20)
    private String phone;

    // @Column con updatable = false significa que JPA nunca modificará
    // este campo en un UPDATE — solo se escribe al crear el registro
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // @PrePersist se ejecuta automáticamente justo ANTES de que JPA
    // haga el INSERT. Así no tienes que setear las fechas manualmente.
    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // @PreUpdate se ejecuta automáticamente justo ANTES de cada UPDATE.
    // Actualiza updated_at cada vez que se modifica el registro.
    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
