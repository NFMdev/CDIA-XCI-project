package com.github.NFMdev.cdia.ingestion_service.integration;

import com.github.NFMdev.cdia.common.dto.EventDto;
import com.github.NFMdev.cdia.ingestion_service.model.event.EventEntity;
import com.github.NFMdev.cdia.ingestion_service.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FullIngestionIntegrationTest {

    @Autowired
    private EventService eventService;

    @Test
    void testFullEventFlow() {
        EventDto dto = new EventDto();
        dto.setDescription("Full Flow Event");
        dto.setLocation("Full Flow Location");

        EventDto saved = eventService.saveEvent(dto);

        assertNotNull(saved.getId());
    }
}
