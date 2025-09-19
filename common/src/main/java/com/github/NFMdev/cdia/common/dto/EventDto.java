package com.github.NFMdev.cdia.common.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class EventDto {
    private Long id;
    private String description;
    private String location;
    private Map<String, Object> payload;
    private String status;
    private LocalDateTime createdAt;
    private Set<MetadataDto> metadata;
    private List<ImageDto> images;
    private Set<AnomalyDto> anomalies;
    private String sourceSystem;
}
