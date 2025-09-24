package com.github.NFMdev.cdia.ingestion_service.model.event;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"event"})
public class EventImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(nullable = false)
    private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventImageEntity)) return false;
        return id != null && id.equals(((EventImageEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
