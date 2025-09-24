package com.github.NFMdev.cdia.ingestion_service.repository;

import com.github.NFMdev.cdia.ingestion_service.model.source_system.SourceSystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SourceSystemRepository extends JpaRepository<SourceSystemEntity, Long> {
    Optional<SourceSystemEntity> findByName(String name);
}
