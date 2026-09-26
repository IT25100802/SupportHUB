package com.customersupport.SupportHUB.faq;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class FuzzyJaccardMatchStrategy implements FaqMatchingStrategy {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "is", "are", "was", "were", "be", "been", "being",
            "in", "on", "at", "to", "for", "of", "with", "by", "from", "up", "about",
            "into", "over", "after", "how", "what", "where", "when", "why", "who",
            "can", "could", "would", "should", "do", "does", "did", "i", "my", "me"
    ));

    @Override
    public MatchResult match(String userQuestion, List<FaqArticle> faqs) {
        if (userQuestion == null || userQuestion.trim().isEmpty() || faqs == null) {
            return MatchResult.noMatch("FuzzyJaccardMatchStrategy");
        }

        Set<String> queryTokens = tokenize(userQuestion);
        if (queryTokens.isEmpty()) {
            return MatchResult.noMatch("FuzzyJaccardMatchStrategy");
        }

        FaqArticle bestFaq = null;
        double bestJaccard = 0.0;

        for (FaqArticle faq : faqs) {
            Set<String> faqTokens = tokenize(faq.getQuestion() + " " + (faq.getKeywords() != null ? faq.getKeywords() : ""));
            double similarity = calculateJaccard(queryTokens, faqTokens);

            if (similarity > bestJaccard) {
                bestJaccard = similarity;
                bestFaq = faq;
            }
        }

        if (bestFaq != null && bestJaccard >= 0.25) {
            return new MatchResult(true, bestFaq, bestJaccard, "FuzzyJaccardMatchStrategy");
        }

        return MatchResult.noMatch("FuzzyJaccardMatchStrategy");
    }

    private Set<String> tokenize(String text) {
        if (text == null) return Collections.emptySet();
        return Arrays.stream(text.toLowerCase().replaceAll("[^a-zA-Z0-9\\s]", " ").split("\\s+"))
                .map(String::trim)
                .filter(word -> word.length() > 1 && !STOP_WORDS.contains(word))
                .collect(Collectors.toSet());
    }

    private double calculateJaccard(Set<String> s1, Set<String> s2) {
        if (s1.isEmpty() || s2.isEmpty()) return 0.0;
        Set<String> intersection = new HashSet<>(s1);
        intersection.retainAll(s2);
        Set<String> union = new HashSet<>(s1);
        union.addAll(s2);
        return (double) intersection.size() / union.size();
    }
}
