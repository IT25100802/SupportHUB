package com.customersupport.SupportHUB.ticket;

import jakarta.validation.constraints.NotBlank;

public class CreateReplyRequest {

    @NotBlank(message = "Message is required")
    private String message;

    private boolean internal = false;

    public CreateReplyRequest() {
    }

    public CreateReplyRequest(String message, boolean internal) {
        this.message = message;
        this.internal = internal;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }
}
