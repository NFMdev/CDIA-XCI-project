package com.github.NFMdev.cdia.reports_service.repository.elasticsearch;

import com.github.NFMdev.cdia.reports_service.model.EventAnomalyDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventAnomalyRepository extends ElasticsearchRepository<EventAnomalyDocument, String> {
    List<EventAnomalyDocument> findTop10ByOrderByDetectedAtDesc();
}