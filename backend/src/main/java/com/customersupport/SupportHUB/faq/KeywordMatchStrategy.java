package com.customersupport.SupportHUB.knowledgebase;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class KeywordMatchStrategy implements FaqMatchingStrategy {

    @Override
    public MatchResult match(String userQuestion, List<FaqArticle> faqs) {
        if (userQuestion == null || userQuestion.trim().isEmpty() || faqs == null) {
            return MatchResult.noMatch("KeywordMatchStrategy");
        }

        String cleanedQuestion = userQuestion.trim().toLowerCase();
        FaqArticle bestFaq = null;
        double maxScore = 0.0;

        for (FaqArticle faq : faqs) {
            double score = 0.0;
            if (faq.getKeywords() != null && !faq.getKeywords().trim().isEmpty()) {
                List<String> keywords = Arrays.stream(faq.getKeywords().split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .filter(k -> !k.isEmpty())
                        .toList();

                int matches = 0;
                for (String kw : keywords) {
                    if (cleanedQuestion.contains(kw)) {
                        matches++;
                    }
                }
                if (!keywords.isEmpty()) {
                    score = (double) matches / keywords.size();
                }
            }

            if (score > maxScore) {
                maxScore = score;
                bestFaq = faq;
            }
        }

        if (bestFaq != null && maxScore >= 0.3) {
            return new MatchResult(true, bestFaq, Math.min(maxScore, 0.95), "KeywordMatchStrategy");
        }

        return MatchResult.noMatch("KeywordMatchStrategy");
    }
}
