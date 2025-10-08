package com.github.NFMdev.cdia.search_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "event-anomalies")
public class EventAnomalyDocument {
    @Id
    private UUID id;
    private String location;
    public long eventCount;
    public String windowStart;
    public String windowEnd;
    public String detectedAt;
    public String rule;
    public String severity;
    public String description;
}
