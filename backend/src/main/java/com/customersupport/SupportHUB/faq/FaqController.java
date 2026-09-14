package com.customersupport.SupportHUB.faq;

import com.customersupport.SupportHUB.common.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
public class FaqController {

    private final FaqService faqService;

    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<FaqArticleDto>> createFaq(
            Authentication authentication,
            @Valid @RequestBody CreateFaqRequest request) {
        FaqArticleDto faq = faqService.createFaq(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("FAQ article created successfully", faq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FaqArticleDto>> getFaqById(@PathVariable("id") Long id) {
        FaqArticleDto faq = faqService.getFaqById(id);
        return ResponseEntity.ok(ApiResponse.success("FAQ article fetched successfully", faq));
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<List<FaqArticleDto>>> getAllFaqs() {
        List<FaqArticleDto> faqs = faqService.getAllFaqs();
        return ResponseEntity.ok(ApiResponse.success("All FAQs fetched successfully", faqs));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<List<FaqArticleDto>>> getPublishedFaqs() {
        List<FaqArticleDto> faqs = faqService.getPublishedFaqs();
        return ResponseEntity.ok(ApiResponse.success("Published FAQs fetched successfully", faqs));
    }

    @GetMapping("/public/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<FaqArticleDto>>> getFaqsByCategory(@PathVariable("categoryId") Long categoryId) {
        List<FaqArticleDto> faqs = faqService.getFaqsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("FAQs for category fetched successfully", faqs));
    }

    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse<List<FaqArticleDto>>> searchFaqs(@RequestParam("q") String q) {
        List<FaqArticleDto> faqs = faqService.searchFaqs(q);
        return ResponseEntity.ok(ApiResponse.success("FAQ search completed", faqs));
    }

    @GetMapping("/public/popular")
    public ResponseEntity<ApiResponse<List<FaqArticleDto>>> getPopularFaqs() {
        List<FaqArticleDto> faqs = faqService.getPopularFaqs();
        return ResponseEntity.ok(ApiResponse.success("Popular FAQs fetched successfully", faqs));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<FaqArticleDto>> updateFaq(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateFaqRequest request) {
        FaqArticleDto updated = faqService.updateFaq(id, request);
        return ResponseEntity.ok(ApiResponse.success("FAQ article updated successfully", updated));
    }

    @PatchMapping("/{id}/toggle-published")
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<FaqArticleDto>> toggleFaqPublished(@PathVariable("id") Long id) {
        FaqArticleDto toggled = faqService.toggleFaqPublished(id);
        return ResponseEntity.ok(ApiResponse.success("FAQ published status toggled", toggled));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteFaq(@PathVariable("id") Long id) {
        faqService.deleteFaq(id);
        return ResponseEntity.ok(ApiResponse.success("FAQ article deleted successfully"));
    }
}
