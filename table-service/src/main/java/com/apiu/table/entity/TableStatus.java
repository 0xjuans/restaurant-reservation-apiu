package com.apiu.table.entity;

// Estados posibles de una mesa. El reservation-service cambiará el estado
// a RESERVED/OCCUPIED cuando procese una reserva.
public enum TableStatus {
    AVAILABLE,
    RESERVED,
    OCCUPIED,
    MAINTENANCE
}
