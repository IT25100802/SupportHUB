package com.customersupport.SupportHUB.feedback;

import com.customersupport.SupportHUB.agent.SupportAgent;
import com.customersupport.SupportHUB.agent.SupportAgentRepository;
import com.customersupport.SupportHUB.customer.CustomerRepository;
import com.customersupport.SupportHUB.faq.FaqArticleRepository;
import com.customersupport.SupportHUB.faq.KnowledgeBaseArticleRepository;
import com.customersupport.SupportHUB.ticket.Ticket;
import com.customersupport.SupportHUB.ticket.TicketRepository;
import com.customersupport.SupportHUB.ticket.TicketStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final FeedbackRepository feedbackRepository;
    private final SupportAgentRepository agentRepository;
    private final FaqArticleRepository faqRepository;
    private final KnowledgeBaseArticleRepository kbRepository;
    private final ReportFactory reportFactory;

    public ReportServiceImpl(
            CustomerRepository customerRepository,
            TicketRepository ticketRepository,
            FeedbackRepository feedbackRepository,
            SupportAgentRepository agentRepository,
            FaqArticleRepository faqRepository,
            KnowledgeBaseArticleRepository kbRepository,
            ReportFactory reportFactory) {
        this.customerRepository = customerRepository;
        this.ticketRepository = ticketRepository;
        this.feedbackRepository = feedbackRepository;
        this.agentRepository = agentRepository;
        this.faqRepository = faqRepository;
        this.kbRepository = kbRepository;
        this.reportFactory = reportFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardReportDto getDashboardReport() {
        long totalCustomers = customerRepository.count();
        long totalTickets = ticketRepository.count();
        long openTickets = ticketRepository.countByStatus(TicketStatus.OPEN);
        long inProgressTickets = ticketRepository.countByStatus(TicketStatus.IN_PROGRESS);
        long waitingTickets = ticketRepository.countByStatus(TicketStatus.WAITING_FOR_CUSTOMER);
        long resolvedTickets = ticketRepository.countByStatus(TicketStatus.RESOLVED);
        long closedTickets = ticketRepository.countByStatus(TicketStatus.CLOSED);
        long activeTickets = openTickets + inProgressTickets;
        long escalatedTickets = ticketRepository.findByIsEscalatedTrue().size();

        Double avgRating = feedbackRepository.calculateAverageRating();
        double averageRating = avgRating != null ? avgRating : 0.0;
        long totalFeedback = feedbackRepository.count();

        long publishedFaqs = faqRepository.findByPublishedTrue().size();
        long knowledgeArticles = kbRepository.findByPublishedTrue().size();

        double resolutionRate = totalTickets > 0 
                ? ((double) (resolvedTickets + closedTickets) / totalTickets) * 100.0 
                : 0.0;

        // Calculate average resolution time in days for resolved/closed tickets
        List<Ticket> allTickets = ticketRepository.findAll();
        double avgResolutionTimeDays = allTickets.stream()
                .filter(t -> t.getResolvedAt() != null && t.getCreatedAt() != null)
                .mapToLong(t -> Math.max(0, Duration.between(t.getCreatedAt(), t.getResolvedAt()).toSeconds()))
                .average()
                .orElse(0.0) / 86400.0;

        List<Object[]> statusCounts = ticketRepository.countTicketsGroupedByStatus();
        List<Object[]> priorityCounts = ticketRepository.countTicketsGroupedByPriority();
        List<Object[]> categoryCounts = ticketRepository.countTicketsGroupedByCategory();

        Map<String, Long> agentWorkload = new HashMap<>();
        List<Map<String, Object>> agentWorkloadDetails = new ArrayList<>();
        List<SupportAgent> agents = agentRepository.findAll();

        for (SupportAgent agent : agents) {
            long activeCount = ticketRepository.countActiveTicketsForAgent(agent.getId());
            agentWorkload.put(agent.getFullName(), activeCount);

            long totalAssigned = allTickets.stream()
                    .filter(t -> t.getAssignedAgent() != null && t.getAssignedAgent().getId().equals(agent.getId()))
                    .count();
            long resolvedCount = allTickets.stream()
                    .filter(t -> t.getAssignedAgent() != null && t.getAssignedAgent().getId().equals(agent.getId()))
                    .filter(t -> t.getStatus() == TicketStatus.RESOLVED || t.getStatus() == TicketStatus.CLOSED)
                    .count();

            Map<String, Object> details = new HashMap<>();
            details.put("id", agent.getId());
            details.put("name", agent.getFullName());
            details.put("employeeCode", agent.getEmployeeCode());
            details.put("status", agent.getStatus() != null ? agent.getStatus().name() : "AVAILABLE");
            details.put("activeTickets", activeCount);
            details.put("totalTickets", totalAssigned);
            details.put("resolvedTickets", resolvedCount);
            details.put("categories", agent.getAssignedCategories().stream().map(c -> c.getName()).toList());

            agentWorkloadDetails.add(details);
        }

        // CSAT Trend points from real Feedback records
        List<Feedback> feedbackList = feedbackRepository.findAll();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
        Map<String, List<Feedback>> groupedByDate = feedbackList.stream()
                .filter(f -> f.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        f -> f.getCreatedAt().format(dtf),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<Map<String, Object>> csatTrend = new ArrayList<>();
        groupedByDate.forEach((dateLabel, list) -> {
            double avg = list.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
            long pos = list.stream().filter(f -> f.getRating() >= 4).count();
            double csatPct = list.isEmpty() ? 0.0 : ((double) pos / list.size()) * 100.0;

            Map<String, Object> point = new HashMap<>();
            point.put("date", dateLabel);
            point.put("count", list.size());
            point.put("avgRating", Math.round(avg * 10.0) / 10.0);
            point.put("csatPercentage", Math.round(csatPct * 10.0) / 10.0);
            csatTrend.add(point);
        });

        return reportFactory.createDashboardReport(
                totalCustomers,
                totalTickets,
                activeTickets,
                openTickets,
                inProgressTickets,
                waitingTickets,
                escalatedTickets,
                resolvedTickets,
                closedTickets,
                averageRating,
                totalFeedback,
                publishedFaqs,
                knowledgeArticles,
                resolutionRate,
                avgResolutionTimeDays,
                statusCounts,
                priorityCounts,
                categoryCounts,
                agentWorkload,
                agentWorkloadDetails,
                csatTrend
        );
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackReportDto getFeedbackReport() {
        Double avgRating = feedbackRepository.calculateAverageRating();
        double averageRating = avgRating != null ? avgRating : 0.0;
        long totalFeedback = feedbackRepository.count();
        List<Object[]> ratingCounts = feedbackRepository.countFeedbackGroupedByRating();

        List<Feedback> allFeedback = feedbackRepository.findAll();

        // Satisfaction by Category
        Map<String, List<Feedback>> byCat = allFeedback.stream()
                .filter(f -> f.getTicket() != null && f.getTicket().getCategory() != null)
                .collect(Collectors.groupingBy(f -> f.getTicket().getCategory().getName()));

        List<Map<String, Object>> categorySatList = new ArrayList<>();
        byCat.forEach((catName, list) -> {
            double avg = list.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
            long pos = list.stream().filter(f -> f.getRating() >= 4).count();
            double pct = list.isEmpty() ? 0.0 : ((double) pos / list.size()) * 100.0;

            Map<String, Object> item = new HashMap<>();
            item.put("categoryName", catName);
            item.put("feedbackCount", list.size());
            item.put("avgRating", Math.round(avg * 10.0) / 10.0);
            item.put("csatPercentage", Math.round(pct * 10.0) / 10.0);
            categorySatList.add(item);
        });
        categorySatList.sort((a, b) -> Double.compare((Double) b.get("avgRating"), (Double) a.get("avgRating")));

        // Officer Performance
        Map<String, List<Feedback>> byAgent = allFeedback.stream()
                .filter(f -> f.getTicket() != null && f.getTicket().getAssignedAgent() != null)
                .collect(Collectors.groupingBy(f -> f.getTicket().getAssignedAgent().getFullName()));

        List<Map<String, Object>> officerPerfList = new ArrayList<>();
        byAgent.forEach((agentName, list) -> {
            double avg = list.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
            long pos = list.stream().filter(f -> f.getRating() >= 4).count();
            double pct = list.isEmpty() ? 0.0 : ((double) pos / list.size()) * 100.0;

            Map<String, Object> item = new HashMap<>();
            item.put("agentName", agentName);
            item.put("feedbackCount", list.size());
            item.put("avgRating", Math.round(avg * 10.0) / 10.0);
            item.put("csatPercentage", Math.round(pct * 10.0) / 10.0);
            officerPerfList.add(item);
        });
        officerPerfList.sort((a, b) -> Double.compare((Double) b.get("avgRating"), (Double) a.get("avgRating")));

        // Trend List
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd");
        Map<String, List<Feedback>> groupedByDate = allFeedback.stream()
                .filter(f -> f.getCreatedAt() != null)
                .collect(Collectors.groupingBy(
                        f -> f.getCreatedAt().format(dtf),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<Map<String, Object>> trendList = new ArrayList<>();
        groupedByDate.forEach((dateLabel, list) -> {
            double avg = list.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
            long pos = list.stream().filter(f -> f.getRating() >= 4).count();
            double csatPct = list.isEmpty() ? 0.0 : ((double) pos / list.size()) * 100.0;

            Map<String, Object> point = new HashMap<>();
            point.put("date", dateLabel);
            point.put("count", list.size());
            point.put("avgRating", Math.round(avg * 10.0) / 10.0);
            point.put("csatPercentage", Math.round(csatPct * 10.0) / 10.0);
            trendList.add(point);
        });

        return reportFactory.createFeedbackReport(
                averageRating,
                totalFeedback,
                ratingCounts,
                categorySatList,
                officerPerfList,
                trendList
        );
    }
}
