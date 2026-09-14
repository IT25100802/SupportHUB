package com.customersupport.SupportHUB.ticket;

import com.customersupport.SupportHUB.agent.AgentStatus;
import com.customersupport.SupportHUB.agent.SupportAgent;
import com.customersupport.SupportHUB.agent.SupportAgentRepository;
import com.customersupport.SupportHUB.category.TicketCategory;
import com.customersupport.SupportHUB.category.TicketCategoryRepository;
import com.customersupport.SupportHUB.common.AccessDeniedException;
import com.customersupport.SupportHUB.common.Attachment;
import com.customersupport.SupportHUB.common.AttachmentRepository;
import com.customersupport.SupportHUB.common.BadRequestException;
import com.customersupport.SupportHUB.common.FileUploadException;
import com.customersupport.SupportHUB.common.ResourceNotFoundException;
import com.customersupport.SupportHUB.common.Role;
import com.customersupport.SupportHUB.common.User;
import com.customersupport.SupportHUB.common.UserRepository;
import com.customersupport.SupportHUB.customer.Customer;
import com.customersupport.SupportHUB.customer.CustomerRepository;
import com.customersupport.SupportHUB.feedback.FeedbackRepository;
import com.customersupport.SupportHUB.notification.Notification;
import com.customersupport.SupportHUB.notification.NotificationFactory;
import com.customersupport.SupportHUB.notification.NotificationService;
import com.customersupport.SupportHUB.notification.NotificationType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final SupportAgentRepository agentRepository;
    private final TicketCategoryRepository categoryRepository;
    private final TicketReplyRepository replyRepository;
    private final TicketHistoryRepository historyRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;
    private final NotificationService notificationService;
    private final NotificationFactory notificationFactory;

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            CustomerRepository customerRepository,
            SupportAgentRepository agentRepository,
            TicketCategoryRepository categoryRepository,
            TicketReplyRepository replyRepository,
            TicketHistoryRepository historyRepository,
            AttachmentRepository attachmentRepository,
            UserRepository userRepository,
            FeedbackRepository feedbackRepository,
            NotificationService notificationService,
            NotificationFactory notificationFactory) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.agentRepository = agentRepository;
        this.categoryRepository = categoryRepository;
        this.replyRepository = replyRepository;
        this.historyRepository = historyRepository;
        this.attachmentRepository = attachmentRepository;
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.notificationService = notificationService;
        this.notificationFactory = notificationFactory;
    }

    @Override
    @Transactional
    public TicketDto createTicket(String customerEmail, CreateTicketRequest request, MultipartFile attachment) {
        Customer customer = customerRepository.findByUserEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer account not found for email: " + customerEmail));

        TicketCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket category not found with id: " + request.getCategoryId()));

        if (!category.isActive()) {
            throw new BadRequestException("Selected category is currently inactive");
        }

        String ticketNumber = generateTicketNumber();

        TicketPriority priority = request.getPriority();
        if (priority == null) {
            priority = evaluateAutomaticPriority(request.getSubject(), request.getDescription());
        }

        Ticket ticket = new Ticket(
                ticketNumber,
                customer,
                category,
                request.getSubject(),
                request.getDescription(),
                priority
        );

        // Automatic Ticket Assignment to AVAILABLE officer in matching category
        List<SupportAgent> availableAgents = agentRepository.findByCategoryId(category.getId()).stream()
                .filter(a -> a.getStatus() == AgentStatus.AVAILABLE && a.getUser().isActive())
                .collect(Collectors.toList());

        if (!availableAgents.isEmpty()) {
            SupportAgent selectedAgent = availableAgents.stream()
                    .min(Comparator.comparingLong(a -> ticketRepository.countActiveTicketsForAgent(a.getId())))
                    .orElse(availableAgents.get(0));

            ticket.setAssignedAgent(selectedAgent);
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }

        Ticket savedTicket = ticketRepository.save(ticket);

        if (savedTicket.getAssignedAgent() != null) {
            recordHistory(savedTicket, customer.getUser(), "TICKET_AUTO_ASSIGNED", "Unassigned", savedTicket.getAssignedAgent().getFullName());
            Notification agentNotif = notificationFactory.createTicketNotification(
                    savedTicket.getAssignedAgent().getUser(), NotificationType.TICKET_ASSIGNED, savedTicket);
            notificationService.sendNotification(agentNotif);
        } else {
            recordHistory(savedTicket, customer.getUser(), "TICKET_CREATED", null, "Status: OPEN, Priority: " + request.getPriority());
        }

        // File Attachment
        if (attachment != null && !attachment.isEmpty()) {
            saveAttachmentFile(savedTicket, null, attachment);
        }

        // Notification
        Notification notification = notificationFactory.createTicketNotification(
                customer.getUser(), NotificationType.TICKET_CREATED, savedTicket);
        notificationService.sendNotification(notification);

        return mapToDto(savedTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDto getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
        return mapToDto(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDto getTicketByNumber(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with number: " + ticketNumber));
        return mapToDto(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketDto> getCustomerTickets(String customerEmail, Pageable pageable) {
        Customer customer = customerRepository.findByUserEmail(customerEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Customer account not found"));
        return ticketRepository.findByCustomerId(customer.getId(), pageable).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketDto> getAssignedOfficerTickets(String officerEmail, Pageable pageable) {
        SupportAgent agent = agentRepository.findByUser(userRepository.findByEmail(officerEmail).orElseThrow())
                .orElseThrow(() -> new ResourceNotFoundException("Support agent record not found"));
        return ticketRepository.findByAssignedAgentId(agent.getId(), pageable).map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketDto> filterTickets(Long customerId, Long agentId, Long categoryId, TicketStatus status, TicketPriority priority, String keyword, Pageable pageable) {
        return ticketRepository.findAll(TicketSpecification.filterTickets(customerId, agentId, categoryId, status, priority, keyword), pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional
    public TicketDto assignAgent(Long ticketId, Long agentId, String performedByEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        SupportAgent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException("Support agent not found with id: " + agentId));

        // Category Restriction Check
        boolean handlesCategory = agent.getAssignedCategories().stream()
                .anyMatch(c -> c.getId().equals(ticket.getCategory().getId()));
        if (!handlesCategory) {
            throw new BadRequestException("Agent '" + agent.getFullName() + "' is not assigned to handle category '" + ticket.getCategory().getName() + "'");
        }

        User performedBy = userRepository.findByEmail(performedByEmail).orElse(null);
        String oldAgent = ticket.getAssignedAgent() != null ? ticket.getAssignedAgent().getFullName() : "Unassigned";

        ticket.setAssignedAgent(agent);
        if (ticket.getStatus() == TicketStatus.OPEN) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }

        Ticket saved = ticketRepository.save(ticket);

        recordHistory(saved, performedBy, "AGENT_ASSIGNED", oldAgent, agent.getFullName());

        // Notifications to Agent and Customer
        Notification agentNotif = notificationFactory.createTicketNotification(
                agent.getUser(), NotificationType.TICKET_ASSIGNED, saved);
        notificationService.sendNotification(agentNotif);

        Notification customerNotif = notificationFactory.createTicketNotification(
                ticket.getCustomer().getUser(), NotificationType.TICKET_ASSIGNED, saved);
        notificationService.sendNotification(customerNotif);

        return mapToDto(saved);
    }

    @Override
    @Transactional
    public TicketDto updateTicketStatus(Long ticketId, UpdateTicketStatusRequest request, String performedByEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        User performedBy = userRepository.findByEmail(performedByEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + performedByEmail));

        TicketStatus oldStatus = ticket.getStatus();
        TicketStatus newStatus = request.getStatus();

        validateStatusTransition(oldStatus, newStatus, performedBy.getRole());

        ticket.setStatus(newStatus);

        if (newStatus == TicketStatus.RESOLVED && ticket.getResolvedAt() == null) {
            ticket.setResolvedAt(LocalDateTime.now());
        } else if (newStatus == TicketStatus.CLOSED && ticket.getClosedAt() == null) {
            ticket.setClosedAt(LocalDateTime.now());
        }

        Ticket saved = ticketRepository.save(ticket);

        String note = request.getNote() != null ? request.getNote() : "";
        recordHistory(saved, performedBy, "STATUS_CHANGED", oldStatus.name(), newStatus.name() + (note.isEmpty() ? "" : " Note: " + note));

        // Notifications
        NotificationType notifType = switch (newStatus) {
            case RESOLVED -> NotificationType.TICKET_RESOLVED;
            case CLOSED -> NotificationType.TICKET_CLOSED;
            default -> NotificationType.TICKET_STATUS_CHANGED;
        };

        Notification customerNotif = notificationFactory.createTicketNotification(
                ticket.getCustomer().getUser(), notifType, saved);
        notificationService.sendNotification(customerNotif);

        return mapToDto(saved);
    }

    @Override
    @Transactional
    public TicketReplyDto addReply(Long ticketId, CreateReplyRequest request, String userEmail, MultipartFile attachment) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new BadRequestException("Cannot reply to a CLOSED ticket");
        }

        // Officer restriction check: if officer replies to internal/external ticket, verify officer category permissions
        if (user.getRole() == Role.CUSTOMER_SERVICE_OFFICER) {
            SupportAgent agent = agentRepository.findByUser(user)
                    .orElseThrow(() -> new AccessDeniedException("User is not an active support officer"));
            boolean handlesCategory = agent.getAssignedCategories().stream()
                    .anyMatch(c -> c.getId().equals(ticket.getCategory().getId()));
            if (!handlesCategory) {
                throw new AccessDeniedException("You do not have permission to reply to tickets in category: " + ticket.getCategory().getName());
            }
        }

        TicketReply reply = new TicketReply(ticket, user, request.getMessage(), request.isInternal());
        TicketReply savedReply = replyRepository.save(reply);

        // Status updates on reply
        if (user.getRole() == Role.CUSTOMER && ticket.getStatus() == TicketStatus.WAITING_FOR_CUSTOMER) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
            ticketRepository.save(ticket);
            recordHistory(ticket, user, "STATUS_CHANGED", "WAITING_FOR_CUSTOMER", "IN_PROGRESS (Customer replied)");
        }

        if (attachment != null && !attachment.isEmpty()) {
            saveAttachmentFile(ticket, savedReply, attachment);
        }

        recordHistory(ticket, user, request.isInternal() ? "INTERNAL_NOTE_ADDED" : "REPLY_POSTED", null, "By " + user.getEmail());

        // Notify customer or agent if not internal
        if (!request.isInternal()) {
            User recipient = user.getRole() == Role.CUSTOMER && ticket.getAssignedAgent() != null
                    ? ticket.getAssignedAgent().getUser() : ticket.getCustomer().getUser();

            Notification replyNotif = notificationFactory.createTicketNotification(
                    recipient, NotificationType.TICKET_REPLIED, ticket);
            notificationService.sendNotification(replyNotif);
        }

        return mapReplyToDto(savedReply);
    }

    @Override
    @Transactional
    public AttachmentDto uploadAttachment(Long ticketId, Long replyId, MultipartFile file) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + ticketId));

        TicketReply reply = null;
        if (replyId != null) {
            reply = replyRepository.findById(replyId).orElse(null);
        }

        Attachment attachment = saveAttachmentFile(ticket, reply, file);
        return mapAttachmentToDto(attachment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketHistoryDto> getTicketHistory(Long ticketId) {
        return historyRepository.findByTicketIdOrderByTimestampDesc(ticketId)
                .stream().map(this::mapHistoryToDto).toList();
    }

    private void validateStatusTransition(TicketStatus current, TicketStatus target, Role role) {
        if (current == target) return;

        if (current == TicketStatus.CLOSED) {
            throw new BadRequestException("Closed tickets cannot be modified further.");
        }

        if (target == TicketStatus.CLOSED && role != Role.CUSTOMER_SERVICE_OFFICER && role != Role.OPERATIONS_SUPERVISOR) {
            throw new AccessDeniedException("Only support officers or supervisors can close tickets.");
        }
    }

    private Attachment saveAttachmentFile(Ticket ticket, TicketReply reply, MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileUploadException("Failed to store empty file");
        }

        try {
            Path root = Paths.get(uploadDir);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String originalName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "attachment.bin";
            String fileExtension = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")) : "";
            String storedName = UUID.randomUUID().toString() + fileExtension;
            Path destination = root.resolve(storedName);

            Files.copy(file.getInputStream(), destination);

            Attachment attachment = new Attachment(
                    ticket,
                    reply,
                    originalName,
                    storedName,
                    file.getContentType(),
                    file.getSize(),
                    destination.toString()
            );

            return attachmentRepository.save(attachment);
        } catch (IOException e) {
            throw new FileUploadException("Could not store file: " + e.getMessage());
        }
    }

    private void recordHistory(Ticket ticket, User performedBy, String action, String oldValue, String newValue) {
        TicketHistory history = new TicketHistory(ticket, performedBy, action, oldValue, newValue);
        historyRepository.save(history);
    }

    private String generateTicketNumber() {
        int year = LocalDateTime.now().getYear();
        long count = ticketRepository.count() + 1;
        return String.format("TKT-%d-%06d", year, count);
    }

    private TicketDto mapToDto(Ticket t) {
        TicketDto dto = new TicketDto();
        dto.setId(t.getId());
        dto.setTicketNumber(t.getTicketNumber());
        dto.setCustomerId(t.getCustomer().getId());
        dto.setCustomerName(t.getCustomer().getFullName());
        dto.setCustomerEmail(t.getCustomer().getUser().getEmail());
        dto.setCategoryId(t.getCategory().getId());
        dto.setCategoryName(t.getCategory().getName());

        if (t.getAssignedAgent() != null) {
            dto.setAssignedAgentId(t.getAssignedAgent().getId());
            dto.setAssignedAgentName(t.getAssignedAgent().getFullName());
        }

        dto.setSubject(t.getSubject());
        dto.setDescription(t.getDescription());
        dto.setPriority(t.getPriority());
        dto.setStatus(t.getStatus());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setUpdatedAt(t.getUpdatedAt());
        dto.setResolvedAt(t.getResolvedAt());
        dto.setClosedAt(t.getClosedAt());

        List<TicketReplyDto> replyDtos = replyRepository.findByTicketIdOrderByCreatedAtAsc(t.getId())
                .stream().map(this::mapReplyToDto).toList();
        dto.setReplies(replyDtos);

        List<AttachmentDto> attachmentDtos = attachmentRepository.findByTicketId(t.getId())
                .stream().map(this::mapAttachmentToDto).toList();
        dto.setAttachments(attachmentDtos);

        List<TicketHistoryDto> historyDtos = historyRepository.findByTicketIdOrderByTimestampDesc(t.getId())
                .stream().map(this::mapHistoryToDto).toList();
        dto.setHistory(historyDtos);

        dto.setHasFeedback(feedbackRepository.existsByTicketId(t.getId()));

        return dto;
    }

    private TicketReplyDto mapReplyToDto(TicketReply r) {
        TicketReplyDto dto = new TicketReplyDto();
        dto.setId(r.getId());
        dto.setTicketId(r.getTicket().getId());
        dto.setUserId(r.getUser().getId());
        dto.setUserName(r.getUser().getEmail());
        dto.setUserRole(r.getUser().getRole());
        dto.setMessage(r.getMessage());
        dto.setInternal(r.isInternal());
        dto.setCreatedAt(r.getCreatedAt());
        return dto;
    }

    private AttachmentDto mapAttachmentToDto(Attachment a) {
        AttachmentDto dto = new AttachmentDto();
        dto.setId(a.getId());
        dto.setTicketId(a.getTicket().getId());
        if (a.getReply() != null) {
            dto.setReplyId(a.getReply().getId());
        }
        dto.setOriginalFileName(a.getOriginalFileName());
        dto.setStoredFileName(a.getStoredFileName());
        dto.setFileType(a.getFileType());
        dto.setFileSize(a.getFileSize());
        dto.setFileUrl("/uploads/" + a.getStoredFileName());
        dto.setUploadedAt(a.getUploadedAt());
        return dto;
    }

    private TicketHistoryDto mapHistoryToDto(TicketHistory h) {
        TicketHistoryDto dto = new TicketHistoryDto();
        dto.setId(h.getId());
        dto.setTicketId(h.getTicket().getId());
        dto.setPerformedByName(h.getPerformedBy() != null ? h.getPerformedBy().getEmail() : "System");
        dto.setAction(h.getAction());
        dto.setOldValue(h.getOldValue());
        dto.setNewValue(h.getNewValue());
        dto.setTimestamp(h.getTimestamp());
        return dto;
    }

    private TicketPriority evaluateAutomaticPriority(String subject, String description) {
        String text = ((subject != null ? subject : "") + " " + (description != null ? description : "")).toLowerCase();

        if (text.contains("system down") || text.contains("crash") || text.contains("500") ||
            text.contains("transaction failed") || text.contains("payment failed") ||
            text.contains("urgent") || text.contains("emergency") || text.contains("outage") ||
            text.contains("locked out") || text.contains("critical")) {
            return TicketPriority.URGENT;
        }

        if (text.contains("bug") || text.contains("error") || text.contains("failed") ||
            text.contains("cannot login") || text.contains("rate limit") || text.contains("delay") ||
            text.contains("broken") || text.contains("supervisor") || text.contains("slow")) {
            return TicketPriority.HIGH;
        }

        if (text.contains("doc") || text.contains("how to") || text.contains("inquiry") ||
            text.contains("info") || text.contains("guide") || text.contains("feature")) {
            return TicketPriority.LOW;
        }

        return TicketPriority.MEDIUM;
    }
}
