package com.customersupport.SupportHUB.ticket;

import com.customersupport.SupportHUB.agent.AssignAgentRequest;
import com.customersupport.SupportHUB.common.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<TicketDto>> createTicket(
            Authentication authentication,
            @Valid @RequestPart(value = "data", required = false) CreateTicketRequest requestData,
            @Valid @RequestPart(value = "request", required = false) CreateTicketRequest requestReq,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        CreateTicketRequest request = requestData != null ? requestData : requestReq;
        TicketDto ticket = ticketService.createTicket(authentication.getName(), request, file);
        return ResponseEntity.ok(ApiResponse.success("Ticket created successfully", ticket));
    }

    @PostMapping(value = "/create-json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<TicketDto>> createTicketJson(
            Authentication authentication,
            @Valid @RequestBody CreateTicketRequest request) {
        TicketDto ticket = ticketService.createTicket(authentication.getName(), request, null);
        return ResponseEntity.ok(ApiResponse.success("Ticket created successfully", ticket));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TicketDto>> getTicketById(@PathVariable("id") Long id) {
        TicketDto ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ApiResponse.success("Ticket details fetched successfully", ticket));
    }

    @GetMapping("/number/{ticketNumber}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TicketDto>> getTicketByNumber(@PathVariable("ticketNumber") String ticketNumber) {
        TicketDto ticket = ticketService.getTicketByNumber(ticketNumber);
        return ResponseEntity.ok(ApiResponse.success("Ticket details fetched successfully", ticket));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Page<TicketDto>>> getMyTickets(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<TicketDto> tickets = ticketService.getCustomerTickets(authentication.getName(), PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(ApiResponse.success("Customer tickets fetched successfully", tickets));
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('CUSTOMER_SERVICE_OFFICER')")
    public ResponseEntity<ApiResponse<Page<TicketDto>>> getAssignedTickets(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<TicketDto> tickets = ticketService.getAssignedOfficerTickets(authentication.getName(), PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(ApiResponse.success("Assigned tickets fetched successfully", tickets));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<TicketDto>>> filterTickets(
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "agentId", required = false) Long agentId,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "status", required = false) TicketStatus status,
            @RequestParam(value = "priority", required = false) TicketPriority priority,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<TicketDto> tickets = ticketService.filterTickets(customerId, agentId, categoryId, status, priority, search, PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(ApiResponse.success("Tickets list fetched successfully", tickets));
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('OPERATIONS_SUPERVISOR')")
    public ResponseEntity<ApiResponse<TicketDto>> assignAgent(
            @PathVariable("id") Long id,
            @Valid @RequestBody AssignAgentRequest request,
            Authentication authentication) {
        TicketDto updated = ticketService.assignAgent(id, request.getAgentId(), authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Support officer assigned successfully", updated));
    }

    @RequestMapping(value = "/{id}/status", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @PreAuthorize("hasAnyRole('CUSTOMER_SERVICE_OFFICER', 'OPERATIONS_SUPERVISOR', 'CUSTOMER_SUPPORT_MANAGER', 'CUSTOMER')")
    public ResponseEntity<ApiResponse<TicketDto>> updateStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request,
            Authentication authentication) {
        TicketDto updated = ticketService.updateTicketStatus(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Ticket status updated successfully", updated));
    }

    @PostMapping("/{id}/replies")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<TicketReplyDto>> addReply(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateReplyRequest request,
            Authentication authentication) {
        TicketReplyDto reply = ticketService.addReply(id, request, authentication.getName(), null);
        return ResponseEntity.ok(ApiResponse.success("Reply added successfully", reply));
    }

    @PostMapping(value = "/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AttachmentDto>> uploadAttachment(
            @PathVariable("id") Long id,
            @RequestParam(value = "replyId", required = false) Long replyId,
            @RequestPart("file") MultipartFile file) {
        AttachmentDto attachment = ticketService.uploadAttachment(id, replyId, file);
        return ResponseEntity.ok(ApiResponse.success("Attachment uploaded successfully", attachment));
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TicketHistoryDto>>> getTicketHistory(@PathVariable("id") Long id) {
        List<TicketHistoryDto> history = ticketService.getTicketHistory(id);
        return ResponseEntity.ok(ApiResponse.success("Ticket history fetched successfully", history));
    }

    @PatchMapping("/{id}/refund-status")
    @PreAuthorize("hasAnyRole('CUSTOMER_SERVICE_OFFICER', 'OPERATIONS_SUPERVISOR', 'CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<TicketDto>> updateRefundStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateRefundStatusRequest request,
            Authentication authentication) {
        TicketDto updated = ticketService.updateRefundStatus(id, request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Refund status updated successfully", updated));
    }

    @GetMapping("/escalated")
    @PreAuthorize("hasAnyRole('OPERATIONS_SUPERVISOR', 'CUSTOMER_SUPPORT_MANAGER', 'QA_EXECUTIVE')")
    public ResponseEntity<ApiResponse<List<TicketDto>>> getEscalatedTickets() {
        List<TicketDto> escalated = ticketService.getEscalatedTickets();
        return ResponseEntity.ok(ApiResponse.success("Escalated tickets fetched successfully", escalated));
    }

    @PostMapping("/check-sla")
    @PreAuthorize("hasAnyRole('OPERATIONS_SUPERVISOR', 'CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<Integer>> checkSlaBreaches() {
        int escalatedCount = ticketService.checkAndEscalateSlaBreaches();
        return ResponseEntity.ok(ApiResponse.success("SLA check evaluated successfully. Escalated count: " + escalatedCount, escalatedCount));
    }
}
