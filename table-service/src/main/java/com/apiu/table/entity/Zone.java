package com.apiu.table.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Catálogo de zonas del restaurante. Se carga con datos iniciales via Flyway.
@Entity
@Table(name = "zones", schema = "tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
