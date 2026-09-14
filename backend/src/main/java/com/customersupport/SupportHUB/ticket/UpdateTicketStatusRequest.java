package com.customersupport.SupportHUB.ticket;

import jakarta.validation.constraints.NotNull;

public class UpdateTicketStatusRequest {

    @NotNull(message = "Status is required")
    private TicketStatus status;

    private String note;

    public UpdateTicketStatusRequest() {
    }

    public UpdateTicketStatusRequest(TicketStatus status, String note) {
        this.status = status;
        this.note = note;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
