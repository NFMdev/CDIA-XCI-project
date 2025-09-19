package com.github.NFMdev.cdia.ingestion_service.mapper;

import com.github.NFMdev.cdia.common.dto.SourceSystemDto;
import com.github.NFMdev.cdia.ingestion_service.config.MapperConfig;
import com.github.NFMdev.cdia.ingestion_service.model.source_system.SourceSystemEntity;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface SourceSystemMapper {
    SourceSystemDto toDto(SourceSystemEntity entity);
    SourceSystemEntity toEntity(SourceSystemDto dto);
}

