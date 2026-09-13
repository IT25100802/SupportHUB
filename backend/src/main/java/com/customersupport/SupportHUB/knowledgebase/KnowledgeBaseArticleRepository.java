package com.customersupport.SupportHUB.knowledgebase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeBaseArticleRepository extends JpaRepository<KnowledgeBaseArticle, Long>, JpaSpecificationExecutor<KnowledgeBaseArticle> {
    List<KnowledgeBaseArticle> findByPublishedTrue();
    List<KnowledgeBaseArticle> findByCategoryIdAndPublishedTrue(Long categoryId);

    @Query("SELECT kb FROM KnowledgeBaseArticle kb WHERE kb.published = true AND (LOWER(kb.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kb.tags) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kb.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<KnowledgeBaseArticle> searchKnowledgeBase(@Param("query") String query);

    List<KnowledgeBaseArticle> findTop5ByPublishedTrueOrderByViewCountDesc();
}
