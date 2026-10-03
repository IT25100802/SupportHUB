package com.customersupport.SupportHUB.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateTicketRequest {

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Priority is required")
    private TicketPriority priority = TicketPriority.MEDIUM;

    private String orderNumber;

    private String trackingNumber;

    private RefundStatus refundStatus = RefundStatus.NONE;

    public CreateTicketRequest() {
    }

    public CreateTicketRequest(Long categoryId, String subject, String description, TicketPriority priority) {
        this.categoryId = categoryId;
        this.subject = subject;
        this.description = description;
        this.priority = priority;
    }

    public CreateTicketRequest(Long categoryId, String subject, String description, TicketPriority priority,
                               String orderNumber, String trackingNumber, RefundStatus refundStatus) {
        this.categoryId = categoryId;
        this.subject = subject;
        this.description = description;
        this.priority = priority;
        this.orderNumber = orderNumber;
        this.trackingNumber = trackingNumber;
        this.refundStatus = refundStatus != null ? refundStatus : RefundStatus.NONE;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }
}
