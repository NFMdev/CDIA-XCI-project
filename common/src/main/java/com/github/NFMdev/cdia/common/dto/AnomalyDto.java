package com.github.NFMdev.cdia.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnomalyDto {
    private Long id;
    private String label;
    private String description;
    private LocalDateTime detectedAt;
    private Double confidenceScore;

    private Long eventId;
}
