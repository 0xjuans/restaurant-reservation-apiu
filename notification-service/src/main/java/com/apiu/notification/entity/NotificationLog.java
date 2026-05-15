package com.apiu.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs", schema = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient_email", nullable = false, length = 150)
    private String recipientEmail;

    // Tipo de notificación almacenado como string para evitar ENUMs en PostgreSQL
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @PrePersist
    void onPersist() {
        sentAt = LocalDateTime.now();
    }
}
