package com.github.NFMdev.cdia.ingestion_service.model.anomaly;

import com.github.NFMdev.cdia.ingestion_service.model.event.EventEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "anomalies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"event", "label"})
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnomalyEntity)) return false;
        return id != null && id.equals(((AnomalyEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
