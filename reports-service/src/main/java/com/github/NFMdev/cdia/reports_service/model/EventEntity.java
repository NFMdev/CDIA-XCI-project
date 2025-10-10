package com.github.NFMdev.cdia.reports_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    private Long id;

    private String description;
    private String location;
    private Long sourceId;
    private Timestamp createdAt;
}

