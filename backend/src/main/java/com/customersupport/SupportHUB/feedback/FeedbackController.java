package com.customersupport.SupportHUB.feedback;

import com.customersupport.SupportHUB.common.ApiResponse;
import com.customersupport.SupportHUB.ticket.Ticket;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<FeedbackDto>> submitFeedback(
            Authentication authentication,
            @Valid @RequestBody CreateFeedbackRequest request) {
        FeedbackDto feedback = feedbackService.submitFeedback(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Feedback submitted successfully", feedback));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('QA_EXECUTIVE', 'CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<FeedbackDto>> getFeedbackById(@PathVariable("id") Long id) {
        FeedbackDto feedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(ApiResponse.success("Feedback details fetched successfully", feedback));
    }

    @GetMapping("/ticket/{ticketId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<FeedbackDto>> getFeedbackByTicketId(@PathVariable("ticketId") Long ticketId) {
        FeedbackDto feedback = feedbackService.getFeedbackByTicketId(ticketId);
        return ResponseEntity.ok(ApiResponse.success("Ticket feedback fetched successfully", feedback));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('QA_EXECUTIVE', 'CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<Page<FeedbackDto>>> filterFeedback(
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<FeedbackDto> feedbackPage = feedbackService.filterFeedback(rating, keyword, PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(ApiResponse.success("Feedback list fetched successfully", feedbackPage));
    }

    @GetMapping("/analytics")
    @PreAuthorize("hasAnyRole('QA_EXECUTIVE', 'CUSTOMER_SUPPORT_MANAGER', 'OPERATIONS_SUPERVISOR')")
    public ResponseEntity<ApiResponse<FeedbackReportDto>> getFeedbackAnalytics() {
        FeedbackReportDto report = feedbackService.getFeedbackAnalytics();
        return ResponseEntity.ok(ApiResponse.success("Feedback CSAT analytics fetched successfully", report));
    }
}
