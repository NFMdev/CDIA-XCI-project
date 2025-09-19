package com.github.NFMdev.cdia.common.dto;

import lombok.Data;

@Data
public class SourceSystemDto {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String apiEndpoint;
}
