package com.github.NFMdev.cdia.ingestion_service.repository;

import com.github.NFMdev.cdia.ingestion_service.model.anomaly.AnomalyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnomalyRepository extends JpaRepository<AnomalyEntity, Long> {
    List<AnomalyEntity> findByEventId(Long eventId);
}
