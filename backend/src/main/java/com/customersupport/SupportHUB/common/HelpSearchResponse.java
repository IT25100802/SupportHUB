package com.customersupport.SupportHUB.common;

import com.customersupport.SupportHUB.category.TicketCategoryDto;
import com.customersupport.SupportHUB.faq.FaqArticleDto;
import com.customersupport.SupportHUB.faq.KnowledgeBaseArticleDto;

import java.util.ArrayList;
import java.util.List;

public class HelpSearchResponse {
    private String query;
    private List<KnowledgeBaseArticleDto> kbArticles = new ArrayList<>();
    private long totalKbCount;
    private List<FaqArticleDto> faqs = new ArrayList<>();
    private long totalFaqCount;
    private List<TicketCategoryDto> categories = new ArrayList<>();
    private String detectedIntent;
    private boolean hasResults;

    public HelpSearchResponse() {
    }

    public HelpSearchResponse(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<KnowledgeBaseArticleDto> getKbArticles() {
        return kbArticles;
    }

    public void setKbArticles(List<KnowledgeBaseArticleDto> kbArticles) {
        this.kbArticles = kbArticles;
    }

    public long getTotalKbCount() {
        return totalKbCount;
    }

    public void setTotalKbCount(long totalKbCount) {
        this.totalKbCount = totalKbCount;
    }

    public List<FaqArticleDto> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<FaqArticleDto> faqs) {
        this.faqs = faqs;
    }

    public long getTotalFaqCount() {
        return totalFaqCount;
    }

    public void setTotalFaqCount(long totalFaqCount) {
        this.totalFaqCount = totalFaqCount;
    }

    public List<TicketCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<TicketCategoryDto> categories) {
        this.categories = categories;
    }

    public String getDetectedIntent() {
        return detectedIntent;
    }

    public void setDetectedIntent(String detectedIntent) {
        this.detectedIntent = detectedIntent;
    }

    public boolean isHasResults() {
        return hasResults;
    }

    public void setHasResults(boolean hasResults) {
        this.hasResults = hasResults;
    }
}
