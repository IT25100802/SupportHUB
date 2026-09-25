package com.customersupport.SupportHUB.common;

import java.util.ArrayList;
import java.util.List;

public class PlatformStatsDto {
    private long totalTickets;
    private long openTickets;
    private long resolvedTickets;
    private String slaComplianceRate;
    private double csatAverage;
    private long publishedGuides;
    private long activeOfficers;
    private List<RecentQueueItemDto> recentQueue = new ArrayList<>();

    public PlatformStatsDto() {
    }

    public long getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(long totalTickets) {
        this.totalTickets = totalTickets;
    }

    public long getOpenTickets() {
        return openTickets;
    }

    public void setOpenTickets(long openTickets) {
        this.openTickets = openTickets;
    }

    public long getResolvedTickets() {
        return resolvedTickets;
    }

    public void setResolvedTickets(long resolvedTickets) {
        this.resolvedTickets = resolvedTickets;
    }

    public String getSlaComplianceRate() {
        return slaComplianceRate;
    }

    public void setSlaComplianceRate(String slaComplianceRate) {
        this.slaComplianceRate = slaComplianceRate;
    }

    public double getCsatAverage() {
        return csatAverage;
    }

    public void setCsatAverage(double csatAverage) {
        this.csatAverage = csatAverage;
    }

    public long getPublishedGuides() {
        return publishedGuides;
    }

    public void setPublishedGuides(long publishedGuides) {
        this.publishedGuides = publishedGuides;
    }

    public long getActiveOfficers() {
        return activeOfficers;
    }

    public void setActiveOfficers(long activeOfficers) {
        this.activeOfficers = activeOfficers;
    }

    public List<RecentQueueItemDto> getRecentQueue() {
        return recentQueue;
    }

    public void setRecentQueue(List<RecentQueueItemDto> recentQueue) {
        this.recentQueue = recentQueue;
    }

    public static class RecentQueueItemDto {
        private String ticketNumber;
        private String subject;
        private String status;
        private String priority;
        private String category;

        public RecentQueueItemDto() {
        }

        public RecentQueueItemDto(String ticketNumber, String subject, String status, String priority, String category) {
            this.ticketNumber = ticketNumber;
            this.subject = subject;
            this.status = status;
            this.priority = priority;
            this.category = category;
        }

        public String getTicketNumber() {
            return ticketNumber;
        }

        public void setTicketNumber(String ticketNumber) {
            this.ticketNumber = ticketNumber;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}
