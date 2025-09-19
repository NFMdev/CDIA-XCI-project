package com.github.NFMdev.cdia.common.dto;

import lombok.Data;

@Data
public class MetadataDto {
    private Long id;
    private String key;
    private String value;

    private Long eventId;
}
