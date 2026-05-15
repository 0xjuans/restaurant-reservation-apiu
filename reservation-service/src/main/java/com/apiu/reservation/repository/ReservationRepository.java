package com.apiu.reservation.repository;

import com.apiu.reservation.entity.Reservation;
import com.apiu.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCustomerId(Long customerId);

    List<Reservation> findByStatus(ReservationStatus status);

    // Detecta solapamiento de horario en la misma mesa y fecha.
    // Dos rangos [s1,e1] y [s2,e2] se solapan cuando s1 < e2 AND e1 > s2.
    @Query("""
            SELECT COUNT(r) > 0 FROM Reservation r
            WHERE r.tableId = :tableId
              AND r.date = :date
              AND r.id <> :excludeId
              AND r.status <> 'CANCELLED'
              AND r.startTime < :endTime
              AND r.endTime > :startTime
            """)
    boolean existsConflict(@Param("tableId") Long tableId,
                           @Param("date") LocalDate date,
                           @Param("startTime") LocalTime startTime,
                           @Param("endTime") LocalTime endTime,
                           @Param("excludeId") Long excludeId);
}
