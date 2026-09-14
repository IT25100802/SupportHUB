package com.customersupport.SupportHUB.faq;

import jakarta.validation.constraints.NotBlank;

public class ChatbotQueryRequest {

    @NotBlank(message = "Question is required")
    private String question;

    public ChatbotQueryRequest() {
    }

    public ChatbotQueryRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
