package com.customersupport.SupportHUB.feedback;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportFactory {

    public DashboardReportDto createDashboardReport(
            long totalCustomers,
            long totalTickets,
            long openTickets,
            long inProgressTickets,
            long resolvedTickets,
            long closedTickets,
            double averageRating,
            long totalFeedbackCount,
            List<Object[]> statusCounts,
            List<Object[]> priorityCounts,
            List<Object[]> categoryCounts,
            Map<String, Long> agentWorkload) {

        DashboardReportDto dto = new DashboardReportDto();
        dto.setTotalCustomers(totalCustomers);
        dto.setTotalTickets(totalTickets);
        dto.setOpenTickets(openTickets);
        dto.setInProgressTickets(inProgressTickets);
        dto.setResolvedTickets(resolvedTickets);
        dto.setClosedTickets(closedTickets);
        dto.setAverageRating(Math.round(averageRating * 10.0) / 10.0);
        dto.setTotalFeedbackCount(totalFeedbackCount);

        Map<String, Long> byStatusMap = new HashMap<>();
        for (Object[] row : statusCounts) {
            byStatusMap.put(row[0].toString(), (Long) row[1]);
        }
        dto.setTicketsByStatus(byStatusMap);

        Map<String, Long> byPriorityMap = new HashMap<>();
        for (Object[] row : priorityCounts) {
            byPriorityMap.put(row[0].toString(), (Long) row[1]);
        }
        dto.setTicketsByPriority(byPriorityMap);

        Map<String, Long> byCategoryMap = new HashMap<>();
        for (Object[] row : categoryCounts) {
            byCategoryMap.put((String) row[0], (Long) row[1]);
        }
        dto.setTicketsByCategory(byCategoryMap);
        dto.setAgentWorkload(agentWorkload != null ? agentWorkload : new HashMap<>());

        return dto;
    }

    public FeedbackReportDto createFeedbackReport(
            double averageRating,
            long totalFeedbackCount,
            List<Object[]> ratingCounts) {

        FeedbackReportDto dto = new FeedbackReportDto();
        dto.setAverageRating(Math.round(averageRating * 10.0) / 10.0);
        dto.setTotalFeedbackCount(totalFeedbackCount);

        Map<Integer, Long> dist = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            dist.put(i, 0L);
        }

        long positiveCount = 0;
        for (Object[] row : ratingCounts) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            dist.put(rating, count);
            if (rating >= 4) {
                positiveCount += count;
            }
        }
        dto.setRatingDistribution(dist);

        double satisfaction = totalFeedbackCount > 0 ? ((double) positiveCount / totalFeedbackCount) * 100.0 : 0.0;
        dto.setSatisfactionPercentage(Math.round(satisfaction * 10.0) / 10.0);

        return dto;
    }
}
