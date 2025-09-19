package com.github.NFMdev.cdia.ingestion_service.repository;


import com.github.NFMdev.cdia.ingestion_service.model.event.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
