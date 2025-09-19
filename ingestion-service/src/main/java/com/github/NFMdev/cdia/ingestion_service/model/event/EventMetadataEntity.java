package com.github.NFMdev.cdia.ingestion_service.model.event;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_metadata")
@Data
@NoArgsConstructor
public class EventMetadataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;
    
    @Column(name = "field_key", nullable = false, length = 100)
    private String key;
    
    @Column(name = "field_value")
    private String value;
}