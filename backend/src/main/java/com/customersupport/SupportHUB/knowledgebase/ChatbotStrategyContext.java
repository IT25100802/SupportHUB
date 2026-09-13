package com.customersupport.SupportHUB.knowledgebase;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatbotStrategyContext {

    private final List<FaqMatchingStrategy> strategies;

    public ChatbotStrategyContext(
            ExactMatchStrategy exactMatchStrategy,
            KeywordMatchStrategy keywordMatchStrategy,
            FuzzyJaccardMatchStrategy fuzzyJaccardMatchStrategy) {
        this.strategies = List.of(exactMatchStrategy, keywordMatchStrategy, fuzzyJaccardMatchStrategy);
    }

    public FaqMatchingStrategy.MatchResult executeMatching(String question, List<FaqArticle> faqs) {
        for (FaqMatchingStrategy strategy : strategies) {
            FaqMatchingStrategy.MatchResult result = strategy.match(question, faqs);
            if (result.isMatched()) {
                return result;
            }
        }
        return FaqMatchingStrategy.MatchResult.noMatch("ChatbotFallback");
    }
}
