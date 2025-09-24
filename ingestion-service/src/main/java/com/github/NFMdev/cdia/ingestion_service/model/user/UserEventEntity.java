package com.github.NFMdev.cdia.ingestion_service.model.user;

import com.github.NFMdev.cdia.ingestion_service.model.event.EventEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// Entity for storing manual validation by users

@Entity
@Table(name = "user_events")
@Data
@NoArgsConstructor
@IdClass(UserEventId.class)
public class UserEventEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    private String operation;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;
}
