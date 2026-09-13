package com.customersupport.SupportHUB.knowledgebase;

import com.customersupport.SupportHUB.common.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge-base")
public class KnowledgeBaseController {

    private final KnowledgeBaseService kbService;

    public KnowledgeBaseController(KnowledgeBaseService kbService) {
        this.kbService = kbService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<KnowledgeBaseArticleDto>> createArticle(
            Authentication authentication,
            @Valid @RequestBody CreateKBRequest request) {
        KnowledgeBaseArticleDto article = kbService.createArticle(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Knowledge base article created successfully", article));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<KnowledgeBaseArticleDto>> getArticleById(@PathVariable Long id) {
        KnowledgeBaseArticleDto article = kbService.getArticleById(id);
        return ResponseEntity.ok(ApiResponse.success("Knowledge base article fetched successfully", article));
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<List<KnowledgeBaseArticleDto>>> getAllArticles() {
        List<KnowledgeBaseArticleDto> articles = kbService.getAllArticles();
        return ResponseEntity.ok(ApiResponse.success("All Knowledge base articles fetched successfully", articles));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<KnowledgeBaseArticleDto>>> getPublishedArticles() {
        List<KnowledgeBaseArticleDto> articles = kbService.getPublishedArticles();
        return ResponseEntity.ok(ApiResponse.success("Published Knowledge base articles fetched successfully", articles));
    }

    @GetMapping("/public/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<KnowledgeBaseArticleDto>>> getArticlesByCategory(@PathVariable Long categoryId) {
        List<KnowledgeBaseArticleDto> articles = kbService.getArticlesByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Knowledge base articles for category fetched successfully", articles));
    }

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse<List<KnowledgeBaseArticleDto>>> searchArticles(@RequestParam String q) {
        List<KnowledgeBaseArticleDto> articles = kbService.searchArticles(q);
        return ResponseEntity.ok(ApiResponse.success("Knowledge base search completed", articles));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<KnowledgeBaseArticleDto>> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody CreateKBRequest request) {
        KnowledgeBaseArticleDto updated = kbService.updateArticle(id, request);
        return ResponseEntity.ok(ApiResponse.success("Knowledge base article updated successfully", updated));
    }

    @PatchMapping("/{id}/toggle-published")
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<KnowledgeBaseArticleDto>> toggleArticlePublished(@PathVariable Long id) {
        KnowledgeBaseArticleDto toggled = kbService.toggleArticlePublished(id);
        return ResponseEntity.ok(ApiResponse.success("Article published status toggled", toggled));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteArticle(@PathVariable Long id) {
        kbService.deleteArticle(id);
        return ResponseEntity.ok(ApiResponse.success("Knowledge base article deleted successfully"));
    }
}
