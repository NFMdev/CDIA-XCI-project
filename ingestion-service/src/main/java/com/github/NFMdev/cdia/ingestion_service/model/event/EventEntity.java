package com.github.NFMdev.cdia.ingestion_service.model.event;

import com.github.NFMdev.cdia.ingestion_service.model.anomaly.AnomalyEntity;
import com.github.NFMdev.cdia.ingestion_service.model.source_system.SourceSystemEntity;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "source_id")
    private SourceSystemEntity sourceSystem;
    
    private String description;
    private String location;
    
    @Type(JsonType.class)
    private Map<String, Object> payload;
    
    @Column(length = 20)
    private String status = "INGESTED";
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventImageEntity> images = new ArrayList<>();

    public void addImage(EventImageEntity image) {
        images.add(image);
        image.setEvent(this);
    }

    public void removeImage(EventImageEntity image) {
        images.remove(image);
        image.setEvent(this);
    }
    
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventMetadataEntity> metadata = new HashSet<>();

    public void addMetadata(EventMetadataEntity meta) {
        metadata.add(meta);
        meta.setEvent(this);
    }

    public void removeMetadata(EventMetadataEntity meta) {
        metadata.remove(meta);
        meta.setEvent(null);
    }
    
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnomalyEntity> anomalies = new HashSet<>();

    public void addAnomaly(AnomalyEntity anomaly) {
        anomalies.add(anomaly);
        anomaly.setEvent(this);
    }

    public void removeAnomaly(AnomalyEntity anomaly) {
        anomalies.remove(anomaly);
        anomaly.setEvent(null);
    }
}