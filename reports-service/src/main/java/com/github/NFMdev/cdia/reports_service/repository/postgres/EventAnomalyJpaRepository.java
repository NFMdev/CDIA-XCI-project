package com.github.NFMdev.cdia.reports_service.repository.postgres;

import com.github.NFMdev.cdia.reports_service.model.EventAnomalyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventAnomalyJpaRepository extends JpaRepository<EventAnomalyEntity, UUID> {

    @Query("SELECT e.location, COUNT(e) FROM EventAnomalyEntity e GROUP BY e.location")
    List<Object[]> countAnomaliesByLocation();

    @Query("SELECT e.severity, COUNT(e) FROM EventAnomalyEntity e GROUP BY e.severity")
    List<Object[]> countAnomaliesBySeverity();
}

