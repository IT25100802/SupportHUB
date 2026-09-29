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
            long activeTickets,
            long openTickets,
            long inProgressTickets,
            long waitingTickets,
            long escalatedTickets,
            long resolvedTickets,
            long closedTickets,
            double averageRating,
            long totalFeedbackCount,
            long publishedFaqs,
            long knowledgeArticles,
            double resolutionRate,
            double avgResolutionTimeDays,
            List<Object[]> statusCounts,
            List<Object[]> priorityCounts,
            List<Object[]> categoryCounts,
            Map<String, Long> agentWorkload,
            List<Map<String, Object>> agentWorkloadDetails,
            List<Map<String, Object>> csatTrend) {

        DashboardReportDto dto = new DashboardReportDto();
        dto.setTotalCustomers(totalCustomers);
        dto.setTotalTickets(totalTickets);
        dto.setActiveTickets(activeTickets);
        dto.setOpenTickets(openTickets);
        dto.setInProgressTickets(inProgressTickets);
        dto.setWaitingTickets(waitingTickets);
        dto.setEscalatedTickets(escalatedTickets);
        dto.setResolvedTickets(resolvedTickets);
        dto.setClosedTickets(closedTickets);
        dto.setAverageRating(Math.round(averageRating * 10.0) / 10.0);
        dto.setTotalFeedbackCount(totalFeedbackCount);
        dto.setPublishedFaqs(publishedFaqs);
        dto.setKnowledgeArticles(knowledgeArticles);
        dto.setResolutionRate(Math.round(resolutionRate * 10.0) / 10.0);
        dto.setAvgResolutionTimeDays(Math.round(avgResolutionTimeDays * 10.0) / 10.0);

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
            if (row[0] != null) {
                byCategoryMap.put((String) row[0], (Long) row[1]);
            }
        }
        dto.setTicketsByCategory(byCategoryMap);
        dto.setAgentWorkload(agentWorkload != null ? agentWorkload : new HashMap<>());
        dto.setAgentWorkloadDetails(agentWorkloadDetails != null ? agentWorkloadDetails : List.of());
        dto.setCsatTrend(csatTrend != null ? csatTrend : List.of());

        return dto;
    }

    public FeedbackReportDto createFeedbackReport(
            double averageRating,
            long totalFeedbackCount,
            List<Object[]> ratingCounts,
            List<Map<String, Object>> categorySatList,
            List<Map<String, Object>> officerPerfList,
            List<Map<String, Object>> trendList) {

        FeedbackReportDto dto = new FeedbackReportDto();
        dto.setAverageRating(Math.round(averageRating * 10.0) / 10.0);
        dto.setTotalFeedbackCount(totalFeedbackCount);

        Map<Integer, Long> dist = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            dist.put(i, 0L);
        }

        long positiveCount = 0;
        long neutralCount = 0;
        long needsAttentionCount = 0;

        for (Object[] row : ratingCounts) {
            Integer rating = (Integer) row[0];
            Long count = (Long) row[1];
            dist.put(rating, count);
            if (rating >= 4) {
                positiveCount += count;
            } else if (rating == 3) {
                neutralCount += count;
            } else {
                needsAttentionCount += count;
            }
        }
        dto.setRatingDistribution(dist);
        dto.setPositiveCount(positiveCount);
        dto.setNeutralCount(neutralCount);
        dto.setNeedsAttentionCount(needsAttentionCount);

        double satisfaction = totalFeedbackCount > 0 ? ((double) positiveCount / totalFeedbackCount) * 100.0 : 0.0;
        dto.setSatisfactionPercentage(Math.round(satisfaction * 10.0) / 10.0);
        dto.setSatisfactionByCategory(categorySatList != null ? categorySatList : List.of());
        dto.setOfficerPerformance(officerPerfList != null ? officerPerfList : List.of());
        dto.setCsatTrend(trendList != null ? trendList : List.of());

        return dto;
    }
}
