package com.github.NFMdev.cdia.ingestion_service.service;

import com.github.NFMdev.cdia.common.dto.EventDto;
import com.github.NFMdev.cdia.ingestion_service.mapper.EventMapper;
import com.github.NFMdev.cdia.ingestion_service.model.event.EventEntity;
import com.github.NFMdev.cdia.ingestion_service.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapper eventMapper;

    public EventDto saveEvent(EventDto eventDto) {
        EventEntity entity = eventMapper.toEntity(eventDto);
        EventEntity saved = eventRepository.save(entity);
        return eventMapper.toDto(saved);
    }

    public EventDto getEventById(Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::toDto)
                .orElse(null);
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
