package com.customersupport.SupportHUB.faq;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private final FaqArticleRepository faqRepository;
    private final KnowledgeBaseArticleRepository kbRepository;
    private final ChatbotStrategyContext strategyContext;

    public ChatbotServiceImpl(FaqArticleRepository faqRepository, 
                              KnowledgeBaseArticleRepository kbRepository,
                              ChatbotStrategyContext strategyContext) {
        this.faqRepository = faqRepository;
        this.kbRepository = kbRepository;
        this.strategyContext = strategyContext;
    }

    @Override
    @Transactional(readOnly = true)
    public ChatbotQueryResponse processQuery(ChatbotQueryRequest request) {
        String rawQuestion = request.getQuestion() != null ? request.getQuestion().trim() : "";
        String lower = rawQuestion.toLowerCase();

        List<FaqArticle> publishedFaqs = faqRepository.findByPublishedTrue();
        FaqMatchingStrategy.MatchResult matchResult = strategyContext.executeMatching(rawQuestion, publishedFaqs);

        ChatbotQueryResponse response = new ChatbotQueryResponse();
        String detectedIntent = detectIntent(lower);
        response.setDetectedIntent(detectedIntent);

        // Search relevant Knowledge Base articles
        List<KnowledgeBaseArticle> kbMatches = kbRepository.searchKnowledgeBase(extractSearchKeyword(lower));
        if (kbMatches == null || kbMatches.isEmpty()) {
            kbMatches = kbRepository.findTop5ByPublishedTrueOrderByViewCountDesc();
        }

        List<KnowledgeBaseArticleDto> kbDtos = kbMatches.stream()
                .filter(KnowledgeBaseArticle::isPublished)
                .limit(3)
                .map(this::mapKbToDto)
                .toList();
        response.setRelatedKbArticles(kbDtos);

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
                    .map(this::mapFaqToDto)
                    .toList();

            response.setRelatedFaqs(related);
        } else {
            response.setMatched(false);
            response.setConfidenceScore(0.0);
            response.setMatchedStrategy("IntentFallbackResponse");
            response.setSuggestTicket(true);

            // Tailored answers based on detected intent
            if ("DELIVERY_INQUIRY".equals(detectedIntent)) {
                response.setAnswer("For delivery status and courier updates, you can track an active shipment using your Order ID or tracking code. If your package is delayed past the estimated date, I can assist you in creating a support ticket.");
            } else if ("REFUND_INQUIRY".equals(detectedIntent)) {
                response.setAnswer("Refunds are processed within 3-5 business days upon item return and inspection. If you have not received your refund for an approved return, I can help submit a ticket for our financial team.");
            } else if ("ORDER_INQUIRY".equals(detectedIntent)) {
                response.setAnswer("For order modifications, cancellations, or damaged item claims, please review our return policies or let me open a support ticket with your Order #.");
            } else if ("PASSWORD_ACCOUNT".equals(detectedIntent)) {
                response.setAnswer("You can manage your account credentials directly in 'My Profile' or use the Forgot Password link on the login screen to reset your password securely.");
            } else {
                response.setAnswer("I couldn't find an exact matching answer in our help center. Would you like to create a support ticket so our technical officers can assist you directly?");
            }

            List<FaqArticleDto> popular = faqRepository.findTop5ByPublishedTrueOrderByViewCountDesc()
                    .stream().map(this::mapFaqToDto).toList();
            response.setRelatedFaqs(popular);
        }

        return response;
    }

    private String detectIntent(String lower) {
        if (lower.contains("track") && (lower.contains("ticket") || lower.contains("status"))) return "TRACK_TICKET";
        if (lower.contains("create") && (lower.contains("ticket") || lower.contains("issue") || lower.contains("complain"))) return "CREATE_TICKET";
        if (lower.contains("refund") || lower.contains("money back") || lower.contains("repayment") || lower.contains("charged twice")) return "REFUND_INQUIRY";
        if (lower.contains("deliver") || lower.contains("ship") || lower.contains("courier") || lower.contains("late") || lower.contains("tracking") || lower.contains("parcel")) return "DELIVERY_INQUIRY";
        if (lower.contains("order") || lower.contains("cancel") || lower.contains("wrong item") || lower.contains("damaged") || lower.contains("return")) return "ORDER_INQUIRY";
        if (lower.contains("password") || lower.contains("login") || lower.contains("account") || lower.contains("2fa") || lower.contains("email")) return "PASSWORD_ACCOUNT";
        if (lower.contains("human") || lower.contains("agent") || lower.contains("officer") || lower.contains("talk") || lower.contains("contact")) return "CONTACT_HUMAN";
        return "GENERAL_INQUIRY";
    }

    private String extractSearchKeyword(String lower) {
        if (lower.contains("refund")) return "refund";
        if (lower.contains("track") || lower.contains("delivery") || lower.contains("shipping")) return "track";
        if (lower.contains("password") || lower.contains("reset")) return "password";
        if (lower.contains("damaged") || lower.contains("faulty") || lower.contains("broken")) return "damaged";
        if (lower.contains("return") || lower.contains("exchange")) return "return";
        if (lower.contains("payment") || lower.contains("card") || lower.contains("method")) return "payment";
        return lower.length() > 3 ? lower.substring(0, Math.min(lower.length(), 20)) : "support";
    }

    private FaqArticleDto mapFaqToDto(FaqArticle f) {
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

    private KnowledgeBaseArticleDto mapKbToDto(KnowledgeBaseArticle kb) {
        KnowledgeBaseArticleDto dto = new KnowledgeBaseArticleDto();
        dto.setId(kb.getId());
        dto.setTitle(kb.getTitle());
        dto.setContent(kb.getContent());
        if (kb.getCategory() != null) {
            dto.setCategoryId(kb.getCategory().getId());
            dto.setCategoryName(kb.getCategory().getName());
        }
        dto.setPublished(kb.isPublished());
        dto.setTags(kb.getTags());
        dto.setViewCount(kb.getViewCount());
        dto.setCreatedAt(kb.getCreatedAt());
        dto.setUpdatedAt(kb.getUpdatedAt());
        return dto;
    }
}
