package com.customersupport.SupportHUB.ticket;

import jakarta.validation.constraints.NotNull;

public class UpdateRefundStatusRequest {

    @NotNull(message = "Refund status is required")
    private RefundStatus refundStatus;

    private String note;

    public UpdateRefundStatusRequest() {
    }

    public UpdateRefundStatusRequest(RefundStatus refundStatus, String note) {
        this.refundStatus = refundStatus;
        this.note = note;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
