package com.github.NFMdev.cdia.reports_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_anomalies")
public class EventAnomalyEntity {
    @Id
    private UUID id;

    private String location;
    public long eventCount;
    public Timestamp windowStart;
    public Timestamp windowEnd;
    public Timestamp detectedAt;
    public String rule;
    public String severity;
    public String description;
}
