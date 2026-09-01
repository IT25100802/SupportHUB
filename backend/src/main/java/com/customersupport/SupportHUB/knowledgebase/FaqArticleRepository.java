package com.customersupport.SupportHUB.knowledgebase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqArticleRepository extends JpaRepository<FaqArticle, Long>, JpaSpecificationExecutor<FaqArticle> {
    List<FaqArticle> findByPublishedTrue();
    List<FaqArticle> findByCategoryIdAndPublishedTrue(Long categoryId);

    @Query("SELECT f FROM FaqArticle f WHERE f.published = true AND (LOWER(f.question) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(f.keywords) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(f.answer) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<FaqArticle> searchFaqs(@Param("query") String query);

    List<FaqArticle> findTop5ByPublishedTrueOrderByViewCountDesc();
}
