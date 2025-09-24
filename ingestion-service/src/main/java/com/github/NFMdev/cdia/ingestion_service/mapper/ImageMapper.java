package com.github.NFMdev.cdia.ingestion_service.mapper;

import com.github.NFMdev.cdia.common.dto.ImageDto;
import com.github.NFMdev.cdia.ingestion_service.config.MapperConfig;
import com.github.NFMdev.cdia.ingestion_service.model.event.EventImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ImageMapper {
    @Mapping(source = "event.id", target = "eventId")
    ImageDto toDto(EventImageEntity entity);

    @Mapping(source = "eventId", target = "event.id")
    EventImageEntity toEntity(ImageDto dto);
}

