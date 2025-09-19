package com.github.NFMdev.cdia.ingestion_service.mapper;

import com.github.NFMdev.cdia.common.dto.UserDto;
import com.github.NFMdev.cdia.ingestion_service.config.MapperConfig;
import com.github.NFMdev.cdia.ingestion_service.model.user.UserEntity;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(UserEntity entity);
    UserEntity toEntity(UserDto dto);
}

