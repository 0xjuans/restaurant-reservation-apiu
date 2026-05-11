CREATE SCHEMA IF NOT EXISTS notifications;

CREATE TYPE notifications.notification_type AS ENUM (
    'RESERVATION_CONFIRMED',
    'RESERVATION_CANCELLED',
    'RESERVATION_REMINDER',
    'RESERVATION_COMPLETED'
);

CREATE TYPE notifications.notification_status AS ENUM (
    'SENT',
    'FAILED'
);

CREATE TABLE notifications.notification_logs (
    id              BIGSERIAL                        PRIMARY KEY,
    recipient_email VARCHAR(150)                     NOT NULL,
    type            notifications.notification_type  NOT NULL,
    reservation_id  BIGINT                           NOT NULL,
    status          notifications.notification_status NOT NULL,
    error_message   VARCHAR(500),
    sent_at         TIMESTAMP                        NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notification_logs_reservation_id ON notifications.notification_logs (reservation_id);
CREATE INDEX idx_notification_logs_sent_at         ON notifications.notification_logs (sent_at);
