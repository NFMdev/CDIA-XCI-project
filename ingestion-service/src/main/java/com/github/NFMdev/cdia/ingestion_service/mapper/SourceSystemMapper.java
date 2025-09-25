package com.github.NFMdev.cdia.ingestion_service.mapper;

import com.github.NFMdev.cdia.ingestion_service.model.source_system.SourceSystemEntity;
import com.github.NFMdev.cdia.ingestion_service.repository.SourceSystemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class SourceSystemMapper {
    @Autowired
    SourceSystemRepository sourceSystemRepository;

    public SourceSystemEntity toEntity(String name) {
        if (name == null) {
            return null;
        }
        return sourceSystemRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException("SourceSystem not found with name: " + name));
    }

    public String toDto(SourceSystemEntity entity) {
        return entity != null ? entity.getName() : null;
    }
}