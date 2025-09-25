package com.github.NFMdev.cdia.ingestion_service.model.event;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"event"})
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventMetadataEntity)) return false;
        return id != null && id.equals(((EventMetadataEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}