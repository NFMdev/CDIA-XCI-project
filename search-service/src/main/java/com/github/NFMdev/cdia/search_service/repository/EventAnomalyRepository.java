package com.github.NFMdev.cdia.search_service.repository;

import com.github.NFMdev.cdia.search_service.model.EventAnomalyDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAnomalyRepository extends ElasticsearchRepository<EventAnomalyDocument, String> {
}
