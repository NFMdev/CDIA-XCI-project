package com.github.NFMdev.cdia.ingestion_service.model.event;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "event_images")
@Data
public class EventImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @Column(nullable = false)
    private String url;
}
