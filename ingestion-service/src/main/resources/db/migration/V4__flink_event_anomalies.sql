-- V4__flink_event_anomalies.sql
-- Flink event anomalies detection

CREATE TABLE event_anomalies (
    id UUID PRIMARY KEY,
    location VARCHAR(255) NOT NULL,
    event_count INT NOT NULL,
    window_start TIMESTAMP NOT NULL,
    window_end TIMESTAMP NOT NULL,
    detected_at TIMESTAMP NOT NULL DEFAULT NOW(),
    rule TEXT NOT NULL, -- e.g. ">10 events in 1 min"
    severity VARCHAR(50), -- e.g. "HIGH", "MEDIUM"
    description TEXT
);
