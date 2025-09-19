package com.github.NFMdev.cdia.ingestion_service.model.anomaly;

import com.github.NFMdev.cdia.ingestion_service.model.event.EventEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "anomalies")
@Data
@NoArgsConstructor
public class AnomalyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "label_id")
    private AnomalyLabelEntity label;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @Column(name = "confidence_score", precision = 5, scale = 2)
    private BigDecimal confidenceScore;

    private String description;

}
