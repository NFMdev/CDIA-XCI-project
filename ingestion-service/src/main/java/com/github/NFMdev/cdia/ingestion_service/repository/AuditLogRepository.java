package com.github.NFMdev.cdia.ingestion_service.repository;

import com.github.NFMdev.cdia.ingestion_service.model.logs.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
    List<AuditLogEntity> findByUserId(Long userId);
}
