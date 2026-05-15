CREATE SCHEMA IF NOT EXISTS notifications;

CREATE TABLE notifications.notification_logs (
    id              BIGSERIAL       PRIMARY KEY,
    recipient_email VARCHAR(150)    NOT NULL,
    type            VARCHAR(50)     NOT NULL,
    reservation_id  BIGINT          NOT NULL,
    status          VARCHAR(20)     NOT NULL,
    error_message   VARCHAR(500),
    sent_at         TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notification_logs_reservation_id ON notifications.notification_logs (reservation_id);
CREATE INDEX idx_notification_logs_sent_at         ON notifications.notification_logs (sent_at);
