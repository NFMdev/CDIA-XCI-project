package com.github.NFMdev.cdia.common.dto;

import lombok.Data;

@Data
public class AuditLogDto {
    private Long id;
    private String operation;
    private String entityType;
    private Long entityId;
    private String performedAt;
    private String username;
}
