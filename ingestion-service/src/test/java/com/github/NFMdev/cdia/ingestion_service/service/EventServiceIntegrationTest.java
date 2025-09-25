package com.github.NFMdev.cdia.ingestion_service.service;

import com.github.NFMdev.cdia.common.dto.EventDto;
import com.github.NFMdev.cdia.ingestion_service.model.event.EventEntity;
import com.github.NFMdev.cdia.ingestion_service.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EventServiceIntegrationTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Test
    void testCreateEvent() {
        EventDto dto = new EventDto();
        dto.setDescription("Integration Test Event");
        dto.setLocation("Integration Location");

        EventDto saved = eventService.saveEvent(dto);

        assertNotNull(saved.getId());
        assertEquals(dto.getDescription(), saved.getDescription());
        assertEquals(dto.getLocation(), saved.getLocation());
        assertTrue(eventRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void testCascadeDelete() {
        EventDto dto = new EventDto();
        dto.setDescription("Delete Cascade Event");
        dto.setLocation("Cascade Location");

        EventDto saved = eventService.saveEvent(dto);
        EventEntity entity = eventRepository.findById(saved.getId()).orElse(null);
        assertNotNull(entity);
        Long eventId = saved.getId();
        eventRepository.delete(entity);

        assertFalse(eventRepository.findById(eventId).isPresent());
    }
}
