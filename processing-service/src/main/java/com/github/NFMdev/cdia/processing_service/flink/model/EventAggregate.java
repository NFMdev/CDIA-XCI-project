package com.github.NFMdev.cdia.processing_service.flink.model;

public class EventAggregate {
    public String location;
    public String eventType;
    public long count;

    public EventAggregate() {}

    public EventAggregate(String location, String eventType, long count) {
        this.location = location;
        this.eventType = eventType;
        this.count = count;
    }
}

