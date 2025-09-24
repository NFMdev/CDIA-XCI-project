package com.github.NFMdev.cdia.ingestion_service.model.anomaly;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "anomaly_labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"anomalies"})
public class AnomalyLabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String code;
    
    private String description;
    
    @OneToMany(mappedBy = "label")
    private Set<AnomalyEntity> anomalies = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnomalyLabelEntity)) return false;
        return id != null && id.equals(((AnomalyLabelEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}