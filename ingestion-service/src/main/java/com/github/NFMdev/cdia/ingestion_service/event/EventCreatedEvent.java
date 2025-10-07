package com.github.NFMdev.cdia.ingestion_service.event;

import com.github.NFMdev.cdia.ingestion_service.model.event.EventEntity;
import org.springframework.context.ApplicationEvent;

public class EventCreatedEvent extends ApplicationEvent {

    private final EventEntity event;

    public EventCreatedEvent(Object source, EventEntity event) {
        super(source);
        this.event = event;
    }

    public EventEntity getEventEntity() {
        return event;
    }
}
