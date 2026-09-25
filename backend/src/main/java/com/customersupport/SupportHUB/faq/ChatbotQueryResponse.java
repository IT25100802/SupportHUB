package com.customersupport.SupportHUB.faq;

import java.util.List;

public class ChatbotQueryResponse {

    private boolean matched;
    private String answer;
    private Double confidenceScore;
    private String matchedStrategy;
    private List<FaqArticleDto> relatedFaqs;
    private List<KnowledgeBaseArticleDto> relatedKbArticles;
    private String detectedIntent;
    private boolean suggestTicket;

    public ChatbotQueryResponse() {
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getMatchedStrategy() {
        return matchedStrategy;
    }

    public void setMatchedStrategy(String matchedStrategy) {
        this.matchedStrategy = matchedStrategy;
    }

    public List<FaqArticleDto> getRelatedFaqs() {
        return relatedFaqs;
    }

    public void setRelatedFaqs(List<FaqArticleDto> relatedFaqs) {
        this.relatedFaqs = relatedFaqs;
    }

    public List<KnowledgeBaseArticleDto> getRelatedKbArticles() {
        return relatedKbArticles;
    }

    public void setRelatedKbArticles(List<KnowledgeBaseArticleDto> relatedKbArticles) {
        this.relatedKbArticles = relatedKbArticles;
    }

    public String getDetectedIntent() {
        return detectedIntent;
    }

    public void setDetectedIntent(String detectedIntent) {
        this.detectedIntent = detectedIntent;
    }

    public boolean isSuggestTicket() {
        return suggestTicket;
    }

    public void setSuggestTicket(boolean suggestTicket) {
        this.suggestTicket = suggestTicket;
    }
}
