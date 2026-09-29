package com.customersupport.SupportHUB.feedback;

import com.customersupport.SupportHUB.common.BadRequestException;
import com.customersupport.SupportHUB.common.DuplicateResourceException;
import com.customersupport.SupportHUB.common.ResourceNotFoundException;
import com.customersupport.SupportHUB.customer.Customer;
import com.customersupport.SupportHUB.customer.CustomerRepository;
import com.customersupport.SupportHUB.ticket.Ticket;
import com.customersupport.SupportHUB.ticket.TicketRepository;
import com.customersupport.SupportHUB.ticket.TicketStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final ReportService reportService;
    private final com.customersupport.SupportHUB.notification.NotificationService notificationService;
    private final com.customersupport.SupportHUB.notification.NotificationFactory notificationFactory;

    public FeedbackServiceImpl(
            FeedbackRepository feedbackRepository,
            TicketRepository ticketRepository,
            CustomerRepository customerRepository,
            ReportService reportService,
            com.customersupport.SupportHUB.notification.NotificationService notificationService,
            com.customersupport.SupportHUB.notification.NotificationFactory notificationFactory) {
        this.feedbackRepository = feedbackRepository;
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.reportService = reportService;
        this.notificationService = notificationService;
        this.notificationFactory = notificationFactory;
    }

    @Override
    @Transactional
    public FeedbackDto submitFeedback(String customerEmail, CreateFeedbackRequest request) {
        Customer customer = customerRepository.findByUserEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer account not found"));

        Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + request.getTicketId()));

        if (!ticket.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException("You can only submit feedback for your own tickets");
        }

        // Restriction Check: Ticket MUST be RESOLVED or CLOSED
        if (ticket.getStatus() != TicketStatus.RESOLVED && ticket.getStatus() != TicketStatus.CLOSED) {
            throw new BadRequestException("Feedback can only be submitted after ticket is RESOLVED or CLOSED. Current status: " + ticket.getStatus());
        }

        // Duplicate Check
        if (feedbackRepository.existsByTicketId(ticket.getId())) {
            throw new DuplicateResourceException("Feedback has already been submitted for ticket " + ticket.getTicketNumber());
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new BadRequestException("Rating must be between 1 and 5 stars");
        }

        Feedback feedback = new Feedback(
                ticket,
                customer,
                request.getRating(),
                request.getComment(),
                request.getSuggestions()
        );

        Feedback saved = feedbackRepository.save(feedback);

        // Send notification to assigned agent
        if (ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getUser() != null) {
            String title = "New Feedback: " + ticket.getTicketNumber();
            String msg = "Customer submitted a " + request.getRating() + "-star rating for ticket '" + ticket.getSubject() + "'.";
            notificationService.sendNotification(
                    notificationFactory.createCustomNotification(
                            ticket.getAssignedAgent().getUser(),
                            title,
                            msg,
                            com.customersupport.SupportHUB.notification.NotificationType.FEEDBACK_RECEIVED,
                            ticket.getId()
                    )
            );
        }

        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackDto getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));
        return mapToDto(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackDto getFeedbackByTicketId(Long ticketId) {
        Feedback feedback = feedbackRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found for ticket id: " + ticketId));
        return mapToDto(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDto> getAllFeedback() {
        return feedbackRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackDto> filterFeedback(Integer rating, String keyword, Pageable pageable) {
        return feedbackRepository.findAll(FeedbackSpecification.filterFeedback(rating, keyword), pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackReportDto getFeedbackAnalytics() {
        return reportService.getFeedbackReport();
    }

    private FeedbackDto mapToDto(Feedback f) {
        FeedbackDto dto = new FeedbackDto();
        dto.setId(f.getId());
        dto.setTicketId(f.getTicket().getId());
        dto.setTicketNumber(f.getTicket().getTicketNumber());
        dto.setTicketSubject(f.getTicket().getSubject());
        if (f.getTicket().getCategory() != null) {
            dto.setCategoryName(f.getTicket().getCategory().getName());
        }
        if (f.getTicket().getAssignedAgent() != null) {
            dto.setAgentName(f.getTicket().getAssignedAgent().getFullName());
        } else {
            dto.setAgentName("Unassigned");
        }
        if (f.getTicket().getStatus() != null) {
            dto.setTicketStatus(f.getTicket().getStatus().name());
        }
        dto.setCustomerId(f.getCustomer().getId());
        dto.setCustomerName(f.getCustomer().getFullName());
        dto.setRating(f.getRating());
        dto.setComment(f.getComment());
        dto.setSuggestions(f.getSuggestions());
        dto.setCreatedAt(f.getCreatedAt());
        return dto;
    }
}
