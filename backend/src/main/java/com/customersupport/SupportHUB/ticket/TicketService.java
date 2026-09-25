package com.customersupport.SupportHUB.ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TicketService {
    TicketDto createTicket(String customerEmail, CreateTicketRequest request, MultipartFile attachment);
    TicketDto getTicketById(Long id);
    TicketDto getTicketByNumber(String ticketNumber);
    Page<TicketDto> getCustomerTickets(String customerEmail, Pageable pageable);
    Page<TicketDto> getAssignedOfficerTickets(String officerEmail, Pageable pageable);
    Page<TicketDto> filterTickets(Long customerId, Long agentId, Long categoryId, TicketStatus status, TicketPriority priority, String keyword, Pageable pageable);
    TicketDto assignAgent(Long ticketId, Long agentId, String performedByEmail);
    TicketDto updateTicketStatus(Long ticketId, UpdateTicketStatusRequest request, String performedByEmail);
    TicketReplyDto addReply(Long ticketId, CreateReplyRequest request, String userEmail, MultipartFile attachment);
    AttachmentDto uploadAttachment(Long ticketId, Long replyId, MultipartFile file);
    List<TicketHistoryDto> getTicketHistory(Long ticketId);

    // E-Commerce & SLA Operations
    TicketDto updateRefundStatus(Long ticketId, UpdateRefundStatusRequest request, String performedByEmail);
    List<TicketDto> getEscalatedTickets();
    int checkAndEscalateSlaBreaches();
}
