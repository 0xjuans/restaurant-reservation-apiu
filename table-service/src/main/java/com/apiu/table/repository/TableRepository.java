package com.apiu.table.repository;

import com.apiu.table.entity.RestaurantTable;
import com.apiu.table.entity.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<RestaurantTable, Long> {

    boolean existsByZoneIdAndNumber(Long zoneId, Integer number);

    List<RestaurantTable> findByStatus(TableStatus status);

    // Mesas disponibles con capacidad mínima para un grupo de N personas
    List<RestaurantTable> findByStatusAndCapacityGreaterThanEqual(TableStatus status, Integer minCapacity);
}
