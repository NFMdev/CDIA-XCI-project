package com.github.NFMdev.cdia.search_service.listener;

import com.github.NFMdev.cdia.ingestion_service.event.EventCreatedEvent;
import com.github.NFMdev.cdia.search_service.mapper.EventDocumentMapper;
import com.github.NFMdev.cdia.search_service.model.EventDocument;
import com.github.NFMdev.cdia.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventIndexListener {

    private final EventDocumentMapper eventDocumentMapper;
    private final SearchService searchService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEventCreated(EventCreatedEvent event) {
        EventDocument doc = eventDocumentMapper.toDocument(event.getEventEntity());
        searchService.index(doc);
        log.info("Indexed EventEntity id={} into Elasticsearch", event.getEventEntity().getId());
    }
}
