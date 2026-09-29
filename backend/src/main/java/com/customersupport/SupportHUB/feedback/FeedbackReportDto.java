package com.customersupport.SupportHUB.feedback;

import java.util.Map;

public class FeedbackReportDto {

    private double averageRating;
    private long totalFeedbackCount;
    private Map<Integer, Long> ratingDistribution; // 1: count, 2: count, etc.
    private double satisfactionPercentage; // Ratings 4 & 5 percentage

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
}
