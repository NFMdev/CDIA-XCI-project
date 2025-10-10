package com.github.NFMdev.cdia.reports_service.service;

import com.github.NFMdev.cdia.reports_service.model.EventAnomalyDocument;
import com.github.NFMdev.cdia.reports_service.repository.elasticsearch.EventAnomalyRepository;
import com.github.NFMdev.cdia.reports_service.repository.postgres.EventAnomalyJpaRepository;
import com.github.NFMdev.cdia.reports_service.repository.postgres.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportService {

    private final EventRepository eventRepository;
    private final EventAnomalyRepository anomalyRepository;
    private final EventAnomalyJpaRepository anomalyJpaRepository;

    public ReportService(EventRepository eventRepository,
                         EventAnomalyRepository anomalyRepository, EventAnomalyJpaRepository anomalyJpaRepository) {
        this.eventRepository = eventRepository;
        this.anomalyRepository = anomalyRepository;
        this.anomalyJpaRepository = anomalyJpaRepository;
    }

    public Map<String, Object> getEventStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEvents", eventRepository.count());
        stats.put("uniqueLocations", eventRepository.countDistinctLocations());
        stats.put("lastEvent", eventRepository.findLastEvent());
        return stats;
    }

    public List<EventAnomalyDocument> getRecentAnomalies() {
        return anomalyRepository.findTop10ByOrderByDetectedAtDesc();
    }

    public Model getDashboardData(Model model) {
        List<Object[]> locationResults = anomalyJpaRepository.countAnomaliesByLocation();
        List<String> locations = locationResults.stream()
                .map(r -> (String) r[0])
                .toList();
        List<Long> eventCounts = locationResults.stream()
                .map(r -> ((Number) r[1]).longValue())
                .toList();

        List<Object[]> severityResults = anomalyJpaRepository.countAnomaliesBySeverity();
        List<String> severities = severityResults.stream()
                .map(r -> (String) r[0])
                .toList();
        List<Long> severityCounts = severityResults.stream()
                .map(r -> ((Number) r[1]).longValue())
                .toList();

        model.addAttribute("locations", locations);
        model.addAttribute("eventCounts", eventCounts);
        model.addAttribute("severities", severities);
        model.addAttribute("severityCounts", severityCounts);

        return model;
    }

}
