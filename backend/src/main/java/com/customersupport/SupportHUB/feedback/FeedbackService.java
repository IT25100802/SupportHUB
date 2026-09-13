package com.customersupport.SupportHUB.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {
    FeedbackDto submitFeedback(String customerEmail, CreateFeedbackRequest request);
    FeedbackDto getFeedbackById(Long id);
    FeedbackDto getFeedbackByTicketId(Long ticketId);
    List<FeedbackDto> getAllFeedback();
    Page<FeedbackDto> filterFeedback(Integer rating, String keyword, Pageable pageable);
    FeedbackReportDto getFeedbackAnalytics();
}
