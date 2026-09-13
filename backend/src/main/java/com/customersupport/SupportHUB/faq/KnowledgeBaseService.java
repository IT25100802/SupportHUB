package com.customersupport.SupportHUB.knowledgebase;

import java.util.List;

public interface KnowledgeBaseService {
    KnowledgeBaseArticleDto createArticle(CreateKBRequest request, String createdByEmail);
    KnowledgeBaseArticleDto getArticleById(Long id);
    List<KnowledgeBaseArticleDto> getAllArticles();
    List<KnowledgeBaseArticleDto> getPublishedArticles();
    List<KnowledgeBaseArticleDto> getArticlesByCategory(Long categoryId);
    List<KnowledgeBaseArticleDto> searchArticles(String query);
    KnowledgeBaseArticleDto updateArticle(Long id, CreateKBRequest request);
    KnowledgeBaseArticleDto toggleArticlePublished(Long id);
    void deleteArticle(Long id);
}
