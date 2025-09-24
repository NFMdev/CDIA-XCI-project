package com.github.NFMdev.cdia.ingestion_service.repository;

import com.github.NFMdev.cdia.ingestion_service.model.source_system.SourceSystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceSystemRepository extends JpaRepository<SourceSystemEntity, Long> {
    SourceSystemEntity findByName(String name);
}
