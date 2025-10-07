package com.github.NFMdev.cdia.search_service.controller;

import com.github.NFMdev.cdia.search_service.model.EventAnomalyDocument;
import com.github.NFMdev.cdia.search_service.model.EventDocument;
import com.github.NFMdev.cdia.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/index")
    public EventDocument index(@RequestBody EventDocument document) {
        return searchService.index(document);
    }

    @GetMapping("/description/{text}")
    public List<EventDocument> searchByDescription(@PathVariable String text) {
        return searchService.searchEventByDescription(text);
    }

    @GetMapping("/location/{location}")
    public List<EventDocument> searchByLocation(@PathVariable String location) {
        return searchService.searchEventByLocation(location);
    }

    @GetMapping("/source-system/{name}")
    public List<EventDocument> searchBySourceSystem(@PathVariable String name) {
        return searchService.searchEventBySourceSystem(name);
    }

    @GetMapping("/events")
    public Iterable<EventDocument> findAllEvents() {
        return searchService.findAllEvents();
    }

    @DeleteMapping("/events")
    public void deleteAllEvents() {
        searchService.deleteAllEvents();
    }

    @GetMapping("/anomalies")
    public Iterable<EventAnomalyDocument> findAll() {
        return searchService.findAllAnomalies();
    }

    @DeleteMapping("/anomalies")
    public void deleteAllAnomalies() {
        searchService.deleteAllEvents();
    }
}
