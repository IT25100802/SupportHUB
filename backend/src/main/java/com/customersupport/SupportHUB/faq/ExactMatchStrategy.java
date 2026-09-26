package com.customersupport.SupportHUB.faq;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExactMatchStrategy implements FaqMatchingStrategy {

    @Override
    public MatchResult match(String userQuestion, List<FaqArticle> faqs) {
        if (userQuestion == null || userQuestion.trim().isEmpty() || faqs == null) {
            return MatchResult.noMatch("ExactMatchStrategy");
        }

        String cleanedQuestion = userQuestion.trim().toLowerCase();

        for (FaqArticle faq : faqs) {
            if (faq.getQuestion() != null && faq.getQuestion().trim().toLowerCase().equals(cleanedQuestion)) {
                return new MatchResult(true, faq, 1.0, "ExactMatchStrategy");
            }
        }

        return MatchResult.noMatch("ExactMatchStrategy");
    }
}
