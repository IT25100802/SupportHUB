package com.customersupport.SupportHUB.feedback;

import java.util.Map;

public class FeedbackReportDto {

    private double averageRating;
    private long totalFeedbackCount;
    private Map<Integer, Long> ratingDistribution; // 1: count, 2: count, etc.
    private double satisfactionPercentage; // Ratings 4 & 5 percentage
    private long positiveCount;
    private long neutralCount;
    private long needsAttentionCount;
    private java.util.List<java.util.Map<String, Object>> satisfactionByCategory;
    private java.util.List<java.util.Map<String, Object>> officerPerformance;
    private java.util.List<java.util.Map<String, Object>> csatTrend;

    public FeedbackReportDto() {
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

    public Map<Integer, Long> getRatingDistribution() {
        return ratingDistribution;
    }

    public void setRatingDistribution(Map<Integer, Long> ratingDistribution) {
        this.ratingDistribution = ratingDistribution;
    }

    public double getSatisfactionPercentage() {
        return satisfactionPercentage;
    }

    public void setSatisfactionPercentage(double satisfactionPercentage) {
        this.satisfactionPercentage = satisfactionPercentage;
    }

    public long getPositiveCount() {
        return positiveCount;
    }

    public void setPositiveCount(long positiveCount) {
        this.positiveCount = positiveCount;
    }

    public long getNeutralCount() {
        return neutralCount;
    }

    public void setNeutralCount(long neutralCount) {
        this.neutralCount = neutralCount;
    }

    public long getNeedsAttentionCount() {
        return needsAttentionCount;
    }

    public void setNeedsAttentionCount(long needsAttentionCount) {
        this.needsAttentionCount = needsAttentionCount;
    }

    public java.util.List<java.util.Map<String, Object>> getSatisfactionByCategory() {
        return satisfactionByCategory;
    }

    public void setSatisfactionByCategory(java.util.List<java.util.Map<String, Object>> satisfactionByCategory) {
        this.satisfactionByCategory = satisfactionByCategory;
    }

    public java.util.List<java.util.Map<String, Object>> getOfficerPerformance() {
        return officerPerformance;
    }

    public void setOfficerPerformance(java.util.List<java.util.Map<String, Object>> officerPerformance) {
        this.officerPerformance = officerPerformance;
    }

    public java.util.List<java.util.Map<String, Object>> getCsatTrend() {
        return csatTrend;
    }

    public void setCsatTrend(java.util.List<java.util.Map<String, Object>> csatTrend) {
        this.csatTrend = csatTrend;
    }
}
