package com.github.NFMdev.cdia.ingestion_service.mapper;

import com.github.NFMdev.cdia.common.dto.AuditLogDto;
import com.github.NFMdev.cdia.ingestion_service.config.MapperConfig;
import com.github.NFMdev.cdia.ingestion_service.model.logs.AuditLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AuditLogMapper {
    @Mapping(source = "user.username", target = "username")
    AuditLogDto toDto(AuditLogEntity entity);

    @Mapping(source = "username", target = "user.username")
    AuditLogEntity toEntity(AuditLogDto dto);
}

