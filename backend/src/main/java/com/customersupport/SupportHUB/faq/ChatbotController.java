package com.customersupport.SupportHUB.faq;

import com.customersupport.SupportHUB.common.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/query")
    public ResponseEntity<ApiResponse<ChatbotQueryResponse>> queryChatbot(@Valid @RequestBody ChatbotQueryRequest request) {
        ChatbotQueryResponse response = chatbotService.processQuery(request);
        return ResponseEntity.ok(ApiResponse.success("Chatbot response generated successfully", response));
    }
}
