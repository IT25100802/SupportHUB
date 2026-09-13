package com.customersupport.SupportHUB.knowledgebase;

import com.customersupport.SupportHUB.common.User;

import java.util.List;

public interface FaqMatchingStrategy {
    
    /**
     * Attempts to match a user question against a list of FAQ articles.
     * @param userQuestion User's raw question string
     * @param faqs List of available published FAQ articles
     * @return Result containing match status, best matched article, confidence score, and strategy name
     */
    MatchResult match(String userQuestion, List<FaqArticle> faqs);

    class MatchResult {
        private boolean matched;
        private FaqArticle matchedArticle;
        private double confidenceScore;
        private String strategyName;

        public MatchResult(boolean matched, FaqArticle matchedArticle, double confidenceScore, String strategyName) {
            this.matched = matched;
            this.matchedArticle = matchedArticle;
            this.confidenceScore = confidenceScore;
            this.strategyName = strategyName;
        }

        public static MatchResult noMatch(String strategyName) {
            return new MatchResult(false, null, 0.0, strategyName);
        }

        public boolean isMatched() {
            return matched;
        }

        public FaqArticle getMatchedArticle() {
            return matchedArticle;
        }

        public double getConfidenceScore() {
            return confidenceScore;
        }

        public String getStrategyName() {
            return strategyName;
        }
    }
}
