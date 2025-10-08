package com.github.NFMdev.cdia.search_service.repository;

import com.github.NFMdev.cdia.search_service.model.EventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventDocumentRepository extends ElasticsearchRepository<EventDocument, String> {
    List<EventDocument> findByDescriptionContainingIgnoreCase(String description);
    List<EventDocument> findByLocationContainingIgnoreCase(String location);
    List<EventDocument> findBySourceSystem(String sourceSystem);
}
