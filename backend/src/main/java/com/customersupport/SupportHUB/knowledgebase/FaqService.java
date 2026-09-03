package com.customersupport.SupportHUB.knowledgebase;

import java.util.List;

public interface FaqService {
    FaqArticleDto createFaq(CreateFaqRequest request, String createdByEmail);
    FaqArticleDto getFaqById(Long id);
    List<FaqArticleDto> getAllFaqs();
    List<FaqArticleDto> getPublishedFaqs();
    List<FaqArticleDto> getFaqsByCategory(Long categoryId);
    List<FaqArticleDto> searchFaqs(String query);
    List<FaqArticleDto> getPopularFaqs();
    FaqArticleDto updateFaq(Long id, CreateFaqRequest request);
    FaqArticleDto toggleFaqPublished(Long id);
    void deleteFaq(Long id);
}
