-- V2__seed_data.sql
-- Seed data for CDIA-XCI-project (safe with auto-generated IDs)

-- 1. Users
INSERT INTO users (username, email, password_hash, role)
VALUES
    ('admin', 'admin@example.com', 'TEST1', 'ADMIN'),
    ('analyst1', 'analyst1@example.com', 'TEST2', 'ANALYST');

-- 2. Source Systems
INSERT INTO source_systems (name, description)
VALUES
    ('CCTV-NorthGate', 'Camera at North Gate'),
    ('MobileApp', 'Citizen reporting app'),
    ('IoT-Sensor-001', 'Environmental sensor in District 1');

-- 3. Anomaly Labels
INSERT INTO anomaly_labels (code, description)
VALUES
    ('UNAUTHORIZED_ACCESS', 'Access attempt without credentials'),
    ('FIRE_DETECTED', 'Possible fire event detected'),
    ('VIOLENCE', 'Physical altercation detected'),
    ('VANDALISM', 'Property damage detected');

-- 4. Events
INSERT INTO events (created_at, source_id, description)
VALUES
    ('2025-09-24 10:00:00',
        (SELECT id FROM source_systems WHERE name = 'CCTV-NorthGate'),
        'Unauthorized access detected'),
    ('2025-09-24 10:05:00',
        (SELECT id FROM source_systems WHERE name = 'MobileApp'),
        'Fire reported via mobile app');

-- 5. Anomalies
INSERT INTO anomalies (event_id, label_id, detected_at, description, confidence_score, severity)
VALUES
    (
        (SELECT id FROM events WHERE created_at = '2025-09-24 10:00:00'),
        (SELECT id FROM anomaly_labels WHERE code = 'UNAUTHORIZED_ACCESS'),
        '2025-09-24 10:00:00',
        'Unauthorized access attempt at north gate',
        99.9,
        'HIGH'
    ),
    (
        (SELECT id FROM events WHERE created_at = '2025-09-24 10:05:00'),
        (SELECT id FROM anomaly_labels WHERE code = 'FIRE_DETECTED'),
        '2025-09-24 10:05:00',
        'Fire reported via mobile app',
        85.0,
        'CRITICAL'
    );

-- 6. Event Images
INSERT INTO event_images (event_id, url)
VALUES
    (
        (SELECT id FROM events WHERE created_at = '2025-09-24 10:00:00'),
        'https://example.com/imgs/event1.jpg'
    ),
    (
        (SELECT id FROM events WHERE created_at = '2025-09-24 10:05:00'),
        'https://example.com/imgs/event2.jpg'
    );

-- 7. Audit Logs
INSERT INTO audit_logs (user_id, operation, entity_type, entity_id, performed_at)
VALUES
    (
        (SELECT id FROM users WHERE username = 'admin'),
        'LOGIN',
        'USERS',
        1,
        NOW()
    ),
    (
        (SELECT id FROM users WHERE username = 'admin'),
        'CREATE_EVENT',
        'EVENTS',
        1,
        NOW()
    );