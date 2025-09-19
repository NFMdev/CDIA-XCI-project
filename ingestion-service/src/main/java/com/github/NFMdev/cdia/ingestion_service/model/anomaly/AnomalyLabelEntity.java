package com.github.NFMdev.cdia.ingestion_service.model.anomaly;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "anomaly_labels")
@Data
@NoArgsConstructor
public class AnomalyLabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String code;
    
    private String description;
    
    @OneToMany(mappedBy = "label")
    private Set<AnomalyEntity> anomalies = new HashSet<>();
}