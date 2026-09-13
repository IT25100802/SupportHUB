package com.customersupport.SupportHUB.knowledgebase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private final FaqArticleRepository faqRepository;
    private final ChatbotStrategyContext strategyContext;

    public ChatbotServiceImpl(FaqArticleRepository faqRepository, ChatbotStrategyContext strategyContext) {
        this.faqRepository = faqRepository;
        this.strategyContext = strategyContext;
    }

    @Override
    @Transactional(readOnly = true)
    public ChatbotQueryResponse processQuery(ChatbotQueryRequest request) {
        List<FaqArticle> publishedFaqs = faqRepository.findByPublishedTrue();

        FaqMatchingStrategy.MatchResult matchResult = strategyContext.executeMatching(request.getQuestion(), publishedFaqs);

        ChatbotQueryResponse response = new ChatbotQueryResponse();

        if (matchResult.isMatched() && matchResult.getMatchedArticle() != null) {
            FaqArticle matchedFaq = matchResult.getMatchedArticle();

            response.setMatched(true);
            response.setAnswer(matchedFaq.getAnswer());
            response.setConfidenceScore(matchResult.getConfidenceScore());
            response.setMatchedStrategy(matchResult.getStrategyName());
            response.setSuggestTicket(false);

            List<FaqArticleDto> related = publishedFaqs.stream()
                    .filter(f -> !f.getId().equals(matchedFaq.getId()))
                    .filter(f -> (matchedFaq.getCategory() != null && f.getCategory() != null && f.getCategory().getId().equals(matchedFaq.getCategory().getId())))
                    .limit(3)
                    .map(this::mapToDto)
                    .toList();

            response.setRelatedFaqs(related);
        } else {
            response.setMatched(false);
            response.setAnswer("I'm sorry, I couldn't find an exact answer to your question in our Knowledge Base. Would you like to submit a support ticket so our technical officers can assist you directly?");
            response.setConfidenceScore(0.0);
            response.setMatchedStrategy("FallbackResponse");
            response.setSuggestTicket(true);

            List<FaqArticleDto> popular = faqRepository.findTop5ByPublishedTrueOrderByViewCountDesc()
                    .stream().map(this::mapToDto).toList();
            response.setRelatedFaqs(popular);
        }

        return response;
    }

    private FaqArticleDto mapToDto(FaqArticle f) {
        FaqArticleDto dto = new FaqArticleDto();
        dto.setId(f.getId());
        dto.setQuestion(f.getQuestion());
        dto.setAnswer(f.getAnswer());
        if (f.getCategory() != null) {
            dto.setCategoryId(f.getCategory().getId());
            dto.setCategoryName(f.getCategory().getName());
        }
        dto.setPublished(f.isPublished());
        dto.setKeywords(f.getKeywords());
        dto.setViewCount(f.getViewCount());
        dto.setCreatedAt(f.getCreatedAt());
        dto.setUpdatedAt(f.getUpdatedAt());
        return dto;
    }
}
