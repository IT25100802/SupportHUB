package com.customersupport.SupportHUB.feedback;

import java.util.List;
import java.util.Map;

public class DashboardReportDto {

    private long totalCustomers;
    private long totalTickets;
    private long activeTickets;
    private long openTickets;
    private long inProgressTickets;
    private long waitingTickets;
    private long escalatedTickets;
    private long resolvedTickets;
    private long closedTickets;
    private double averageRating;
    private long totalFeedbackCount;
    private long publishedFaqs;
    private long knowledgeArticles;
    private double resolutionRate;
    private double avgResolutionTimeDays;
    private Map<String, Long> ticketsByStatus;
    private Map<String, Long> ticketsByPriority;
    private Map<String, Long> ticketsByCategory;
    private Map<String, Long> agentWorkload;
    private List<Map<String, Object>> agentWorkloadDetails;
    private List<Map<String, Object>> csatTrend;

    public DashboardReportDto() {
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(long totalTickets) {
        this.totalTickets = totalTickets;
    }

    public long getActiveTickets() {
        return activeTickets;
    }

    public void setActiveTickets(long activeTickets) {
        this.activeTickets = activeTickets;
    }

    public long getOpenTickets() {
        return openTickets;
    }

    public void setOpenTickets(long openTickets) {
        this.openTickets = openTickets;
    }

    public long getInProgressTickets() {
        return inProgressTickets;
    }

    public void setInProgressTickets(long inProgressTickets) {
        this.inProgressTickets = inProgressTickets;
    }

    public long getWaitingTickets() {
        return waitingTickets;
    }

    public void setWaitingTickets(long waitingTickets) {
        this.waitingTickets = waitingTickets;
    }

    public long getEscalatedTickets() {
        return escalatedTickets;
    }

    public void setEscalatedTickets(long escalatedTickets) {
        this.escalatedTickets = escalatedTickets;
    }

    public long getResolvedTickets() {
        return resolvedTickets;
    }

    public void setResolvedTickets(long resolvedTickets) {
        this.resolvedTickets = resolvedTickets;
    }

    public long getClosedTickets() {
        return closedTickets;
    }

    public void setClosedTickets(long closedTickets) {
        this.closedTickets = closedTickets;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getTotalFeedbackCount() {
        return totalFeedbackCount;
    }

    public void setTotalFeedbackCount(long totalFeedbackCount) {
        this.totalFeedbackCount = totalFeedbackCount;
    }

    public long getPublishedFaqs() {
        return publishedFaqs;
    }

    public void setPublishedFaqs(long publishedFaqs) {
        this.publishedFaqs = publishedFaqs;
    }

    public long getKnowledgeArticles() {
        return knowledgeArticles;
    }

    public void setKnowledgeArticles(long knowledgeArticles) {
        this.knowledgeArticles = knowledgeArticles;
    }

    public double getResolutionRate() {
        return resolutionRate;
    }

    public void setResolutionRate(double resolutionRate) {
        this.resolutionRate = resolutionRate;
    }

    public double getAvgResolutionTimeDays() {
        return avgResolutionTimeDays;
    }

    public void setAvgResolutionTimeDays(double avgResolutionTimeDays) {
        this.avgResolutionTimeDays = avgResolutionTimeDays;
    }

    public Map<String, Long> getTicketsByStatus() {
        return ticketsByStatus;
    }

    public void setTicketsByStatus(Map<String, Long> ticketsByStatus) {
        this.ticketsByStatus = ticketsByStatus;
    }

    public Map<String, Long> getTicketsByPriority() {
        return ticketsByPriority;
    }

    public void setTicketsByPriority(Map<String, Long> ticketsByPriority) {
        this.ticketsByPriority = ticketsByPriority;
    }

    public Map<String, Long> getTicketsByCategory() {
        return ticketsByCategory;
    }

    public void setTicketsByCategory(Map<String, Long> ticketsByCategory) {
        this.ticketsByCategory = ticketsByCategory;
    }

    public Map<String, Long> getAgentWorkload() {
        return agentWorkload;
    }

    public void setAgentWorkload(Map<String, Long> agentWorkload) {
        this.agentWorkload = agentWorkload;
    }

    public List<Map<String, Object>> getAgentWorkloadDetails() {
        return agentWorkloadDetails;
    }

    public void setAgentWorkloadDetails(List<Map<String, Object>> agentWorkloadDetails) {
        this.agentWorkloadDetails = agentWorkloadDetails;
    }

    public List<Map<String, Object>> getCsatTrend() {
        return csatTrend;
    }

    public void setCsatTrend(List<Map<String, Object>> csatTrend) {
        this.csatTrend = csatTrend;
    }
}

