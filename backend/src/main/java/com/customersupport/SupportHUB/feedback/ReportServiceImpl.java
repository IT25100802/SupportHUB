package com.customersupport.SupportHUB.feedback;

import com.customersupport.SupportHUB.agent.SupportAgent;
import com.customersupport.SupportHUB.agent.SupportAgentRepository;
import com.customersupport.SupportHUB.customer.CustomerRepository;
import com.customersupport.SupportHUB.ticket.TicketRepository;
import com.customersupport.SupportHUB.ticket.TicketStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final FeedbackRepository feedbackRepository;
    private final SupportAgentRepository agentRepository;
    private final ReportFactory reportFactory;

    public ReportServiceImpl(
            CustomerRepository customerRepository,
            TicketRepository ticketRepository,
            FeedbackRepository feedbackRepository,
            SupportAgentRepository agentRepository,
            ReportFactory reportFactory) {
        this.customerRepository = customerRepository;
        this.ticketRepository = ticketRepository;
        this.feedbackRepository = feedbackRepository;
        this.agentRepository = agentRepository;
        this.reportFactory = reportFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardReportDto getDashboardReport() {
        long totalCustomers = customerRepository.count();
        long totalTickets = ticketRepository.count();
        long openTickets = ticketRepository.countByStatus(TicketStatus.OPEN);
        long inProgressTickets = ticketRepository.countByStatus(TicketStatus.IN_PROGRESS);
        long resolvedTickets = ticketRepository.countByStatus(TicketStatus.RESOLVED);
        long closedTickets = ticketRepository.countByStatus(TicketStatus.CLOSED);

        Double avgRating = feedbackRepository.calculateAverageRating();
        double averageRating = avgRating != null ? avgRating : 0.0;
        long totalFeedback = feedbackRepository.count();

        List<Object[]> statusCounts = ticketRepository.countTicketsGroupedByStatus();
        List<Object[]> priorityCounts = ticketRepository.countTicketsGroupedByPriority();
        List<Object[]> categoryCounts = ticketRepository.countTicketsGroupedByCategory();

        Map<String, Long> agentWorkload = new HashMap<>();
        List<SupportAgent> agents = agentRepository.findAll();
        for (SupportAgent agent : agents) {
            long count = ticketRepository.countActiveTicketsForAgent(agent.getId());
            agentWorkload.put(agent.getFullName(), count);
        }

        return reportFactory.createDashboardReport(
                totalCustomers,
                totalTickets,
                openTickets,
                inProgressTickets,
                resolvedTickets,
                closedTickets,
                averageRating,
                totalFeedback,
                statusCounts,
                priorityCounts,
                categoryCounts,
                agentWorkload
        );
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackReportDto getFeedbackReport() {
        Double avgRating = feedbackRepository.calculateAverageRating();
        double averageRating = avgRating != null ? avgRating : 0.0;
        long totalFeedback = feedbackRepository.count();
        List<Object[]> ratingCounts = feedbackRepository.countFeedbackGroupedByRating();

        return reportFactory.createFeedbackReport(averageRating, totalFeedback, ratingCounts);
    }
}
