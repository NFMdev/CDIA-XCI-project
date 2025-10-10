package com.github.NFMdev.cdia.reports_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSummary {
    private long totalEvents;
    private long totalAnomalies;
    private String topWorkplace;
    private List<EventAnomalyDocument> recentAnomalies;
}
