package com.github.NFMdev.cdia.ingestion_service.model.source_system;

import com.github.NFMdev.cdia.ingestion_service.model.event.EventEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "source_systems")
@Data
@NoArgsConstructor
public class SourceSystemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 100)
    private String name;
    
    @Column(length = 50)
    private String type;
    
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "api_endpoint")
    private String apiEndpoint;
    
    @OneToMany(mappedBy = "sourceSystem")
    private Set<EventEntity> events = new HashSet<>();
}