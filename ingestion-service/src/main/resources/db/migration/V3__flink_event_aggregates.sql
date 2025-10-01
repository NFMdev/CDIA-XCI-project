-- V2__seed_data.sql
-- Aggregates for events (per type/location/time window)
-- Publication for events

CREATE TABLE event_aggregates (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    window_start TIMESTAMP NOT NULL,
    window_end TIMESTAMP NOT NULL,
    count BIGINT NOT NULL
);

CREATE PUBLICATION flink_publication FOR TABLE events;