package com.customersupport.SupportHUB.customer;

import com.customersupport.SupportHUB.common.Attachment;
import com.customersupport.SupportHUB.common.AttachmentRepository;
import com.customersupport.SupportHUB.common.AuditLog;
import com.customersupport.SupportHUB.common.AuditLogRepository;
import com.customersupport.SupportHUB.common.DuplicateResourceException;
import com.customersupport.SupportHUB.common.PasswordResetTokenRepository;
import com.customersupport.SupportHUB.common.ResourceNotFoundException;
import com.customersupport.SupportHUB.common.Role;
import com.customersupport.SupportHUB.common.User;
import com.customersupport.SupportHUB.common.UserRepository;
import com.customersupport.SupportHUB.feedback.Feedback;
import com.customersupport.SupportHUB.feedback.FeedbackRepository;
import com.customersupport.SupportHUB.notification.Notification;
import com.customersupport.SupportHUB.notification.NotificationRepository;
import com.customersupport.SupportHUB.ticket.Ticket;
import com.customersupport.SupportHUB.ticket.TicketDto;
import com.customersupport.SupportHUB.ticket.TicketHistory;
import com.customersupport.SupportHUB.ticket.TicketHistoryRepository;
import com.customersupport.SupportHUB.ticket.TicketReply;
import com.customersupport.SupportHUB.ticket.TicketReplyRepository;
import com.customersupport.SupportHUB.ticket.TicketRepository;
import com.customersupport.SupportHUB.ticket.TicketStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TicketRepository ticketRepository;
    private final TicketReplyRepository ticketReplyRepository;
    private final TicketHistoryRepository ticketHistoryRepository;
    private final AttachmentRepository attachmentRepository;
    private final FeedbackRepository feedbackRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AuditLogRepository auditLogRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               CustomerOrderRepository customerOrderRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               TicketRepository ticketRepository,
                               TicketReplyRepository ticketReplyRepository,
                               TicketHistoryRepository ticketHistoryRepository,
                               AttachmentRepository attachmentRepository,
                               FeedbackRepository feedbackRepository,
                               NotificationRepository notificationRepository,
                               PasswordResetTokenRepository passwordResetTokenRepository,
                               AuditLogRepository auditLogRepository) {
        this.customerRepository = customerRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ticketRepository = ticketRepository;
        this.ticketReplyRepository = ticketReplyRepository;
        this.ticketHistoryRepository = ticketHistoryRepository;
        this.attachmentRepository = attachmentRepository;
        this.feedbackRepository = feedbackRepository;
        this.notificationRepository = notificationRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public CustomerDto createCustomer(CreateCustomerRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User already exists with email: " + request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        user = userRepository.save(user);

        Customer customer = new Customer(user, request.getFullName(), request.getPhone(), request.getAddress());
        Customer saved = customerRepository.save(customer);

        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByUserId(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for user id: " + userId));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for email: " + email));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::mapToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> searchCustomers(String keyword, Pageable pageable) {
        return customerRepository.findAll(CustomerSpecification.searchCustomers(keyword), pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional
    public CustomerDto updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setFullName(request.getFullName());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());

        Customer updated = customerRepository.save(customer);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        User user = customer.getUser();

        // 1. Delete all tickets created by this customer and their associated records
        List<Ticket> tickets = ticketRepository.findByCustomerId(id);
        for (Ticket ticket : tickets) {
            // Delete feedback on ticket
            feedbackRepository.findByTicketId(ticket.getId()).ifPresent(feedbackRepository::delete);

            // Delete replies and their attachments
            List<TicketReply> replies = ticketReplyRepository.findByTicketIdOrderByCreatedAtAsc(ticket.getId());
            for (TicketReply reply : replies) {
                List<Attachment> replyAttachments = attachmentRepository.findByReplyId(reply.getId());
                if (!replyAttachments.isEmpty()) {
                    for (Attachment a : replyAttachments) {
                        try {
                            if (a.getFilePath() != null) {
                                Files.deleteIfExists(Paths.get(a.getFilePath()));
                            }
                        } catch (Exception ignored) {}
                    }
                    attachmentRepository.deleteAll(replyAttachments);
                }
            }
            ticketReplyRepository.deleteAll(replies);

            // Delete ticket-level attachments
            List<Attachment> ticketAttachments = attachmentRepository.findByTicketId(ticket.getId());
            if (!ticketAttachments.isEmpty()) {
                for (Attachment a : ticketAttachments) {
                    try {
                        if (a.getFilePath() != null) {
                            Files.deleteIfExists(Paths.get(a.getFilePath()));
                        }
                    } catch (Exception ignored) {}
                }
                attachmentRepository.deleteAll(ticketAttachments);
            }

            // Delete ticket audit history
            List<TicketHistory> histories = ticketHistoryRepository.findByTicketIdOrderByTimestampDesc(ticket.getId());
            ticketHistoryRepository.deleteAll(histories);

            // Delete ticket
            ticketRepository.delete(ticket);
        }

        // 2. Delete any customer feedback
        List<Feedback> customerFeedbacks = feedbackRepository.findByCustomerId(id);
        if (!customerFeedbacks.isEmpty()) {
            feedbackRepository.deleteAll(customerFeedbacks);
        }

        // 3. Clean up user-related records
        if (user != null) {
            ticketReplyRepository.deleteByUser(user);

            List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
            if (!notifications.isEmpty()) {
                notificationRepository.deleteAll(notifications);
            }

            passwordResetTokenRepository.deleteByUser(user);
            auditLogRepository.dissociateUser(user);
            ticketHistoryRepository.dissociateUser(user);
        }

        // 4. Delete customer profile
        customerRepository.delete(customer);

        // 5. Delete user account permanently
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Override
    @Transactional
    public int deactivateInactiveCustomers(int daysThreshold) {
        int thresholdDays = daysThreshold > 0 ? daysThreshold : 180;
        LocalDateTime threshold = LocalDateTime.now().minusDays(thresholdDays);
        List<User> inactiveUsers = userRepository.findInactiveCustomers(threshold);
        int deactivatedCount = 0;

        for (User user : inactiveUsers) {
            user.setActive(false);
            userRepository.save(user);

            AuditLog auditLog = new AuditLog(
                    user,
                    "CUSTOMER_AUTO_DEACTIVATED",
                    "User",
                    user.getId(),
                    String.format("Customer account automatically deactivated due to %d+ days of continuous inactivity. Threshold: %d days. Last login: %s",
                            thresholdDays, thresholdDays, user.getLastLoginAt() != null ? user.getLastLoginAt().toString() : "Never logged in")
            );
            auditLogRepository.save(auditLog);
            deactivatedCount++;
            log.info("Automatically deactivated inactive customer: email={}, userId={}, lastLoginAt={}",
                    user.getEmail(), user.getId(), user.getLastLoginAt());
        }
        return deactivatedCount;
    }

    @Override
    @Transactional
    public CustomerDto reactivateCustomer(Long customerId, String performedByEmail) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        User user = customer.getUser();
        user.setActive(true);
        userRepository.save(user);

        User performedBy = performedByEmail != null ? userRepository.findByEmail(performedByEmail).orElse(null) : null;
        AuditLog auditLog = new AuditLog(
                performedBy,
                "CUSTOMER_REACTIVATED",
                "Customer",
                customer.getId(),
                "Customer account manually reactivated by " + (performedByEmail != null ? performedByEmail : "System")
        );
        auditLogRepository.save(auditLog);
        log.info("Reactivated customer: id={}, email={}, performedBy={}", customer.getId(), user.getEmail(), performedByEmail);

        return mapToDto(customer);
    }

    private CustomerDto mapToDto(Customer c) {
        CustomerDto dto = new CustomerDto();
        dto.setId(c.getId());
        dto.setCustomerCode(String.format("CUS-%05d", c.getId()));
        dto.setUserId(c.getUser().getId());
        dto.setEmail(c.getUser().getEmail());
        dto.setFullName(c.getFullName());
        dto.setPhone(c.getPhone());
        dto.setAddress(c.getAddress());
        dto.setActive(c.getUser().isActive());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        dto.setLastLoginAt(c.getUser().getLastLoginAt());

        if (c.getUser().getLastLoginAt() != null) {
            long days = Duration.between(c.getUser().getLastLoginAt(), LocalDateTime.now()).toDays();
            dto.setInactivityDays(Math.max(0, days));
        } else if (c.getUser().getCreatedAt() != null) {
            long days = Duration.between(c.getUser().getCreatedAt(), LocalDateTime.now()).toDays();
            dto.setInactivityDays(Math.max(0, days));
        } else {
            dto.setInactivityDays(0L);
        }

        // Derive Real Support Ticket Stats
        List<Ticket> tickets = ticketRepository.findByCustomerId(c.getId());
        long openCount = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.OPEN)
                .count();
        long inProgressCount = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.IN_PROGRESS || t.getStatus() == TicketStatus.WAITING_FOR_CUSTOMER)
                .count();
        long resolvedCount = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.RESOLVED)
                .count();
        long closedCount = tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.CLOSED)
                .count();

        dto.setOpenTickets(openCount);
        dto.setInProgressTickets(inProgressCount);
        dto.setResolvedTickets(resolvedCount);
        dto.setClosedTickets(closedCount);
        dto.setTotalTickets((long) tickets.size());

        // Derive Real E-Commerce Orders Metrics from database & tickets
        List<CustomerOrder> customerOrders = customerOrderRepository.findByCustomerId(c.getId());
        Set<String> distinctOrderNumbers = new HashSet<>();
        customerOrders.forEach(o -> distinctOrderNumbers.add(o.getOrderNumber()));
        tickets.forEach(t -> {
            if (t.getOrderNumber() != null && !t.getOrderNumber().trim().isEmpty()) {
                distinctOrderNumbers.add(t.getOrderNumber().trim());
            }
        });

        int actualOrderCount = Math.max(c.getTotalOrders() != null ? c.getTotalOrders() : 0, distinctOrderNumbers.size());

        long completed = customerOrders.stream().filter(o -> "COMPLETED".equalsIgnoreCase(o.getOrderStatus())).count();
        long processing = customerOrders.stream().filter(o -> "PROCESSING".equalsIgnoreCase(o.getOrderStatus())).count();
        long cancelled = customerOrders.stream().filter(o -> "CANCELLED".equalsIgnoreCase(o.getOrderStatus())).count();

        if (customerOrders.isEmpty() && actualOrderCount > 0) {
            completed = actualOrderCount;
        }

        dto.setTotalOrders(actualOrderCount);
        dto.setCompletedOrders((int) completed);
        dto.setProcessingOrders((int) processing);
        dto.setCancelledOrders((int) cancelled);

        // Populate Recent Tickets for Customer Profile Command Center
        List<TicketDto> recentList = tickets.stream()
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .limit(5)
                .map(t -> {
                    TicketDto td = new TicketDto();
                    td.setId(t.getId());
                    td.setTicketNumber(t.getTicketNumber());
                    td.setSubject(t.getSubject());
                    td.setStatus(t.getStatus());
                    td.setPriority(t.getPriority());
                    td.setCategoryName(t.getCategory() != null ? t.getCategory().getName() : "General Support");
                    td.setOrderNumber(t.getOrderNumber());
                    td.setCreatedAt(t.getCreatedAt());
                    return td;
                })
                .toList();
        dto.setRecentTickets(recentList);

        return dto;
    }
}
