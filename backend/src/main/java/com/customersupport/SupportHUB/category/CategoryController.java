package com.customersupport.SupportHUB.category;

import com.customersupport.SupportHUB.common.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERATIONS_SUPERVISOR') or hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<TicketCategoryDto>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        TicketCategoryDto category = categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success("Category created successfully", category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketCategoryDto>> getCategoryById(@PathVariable("id") Long id) {
        TicketCategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success("Category fetched successfully", category));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<TicketCategoryDto>>> getAllCategories() {
        List<TicketCategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories list fetched successfully", categories));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TicketCategoryDto>>> getActiveCategories() {
        List<TicketCategoryDto> categories = categoryService.getActiveCategories();
        return ResponseEntity.ok(ApiResponse.success("Active categories fetched successfully", categories));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OPERATIONS_SUPERVISOR') or hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<TicketCategoryDto>> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateCategoryRequest request) {
        TicketCategoryDto updated = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updated));
    }

    @PatchMapping("/{id}/toggle-active")
    @PreAuthorize("hasRole('OPERATIONS_SUPERVISOR') or hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<TicketCategoryDto>> toggleCategoryActive(@PathVariable("id") Long id) {
        TicketCategoryDto toggled = categoryService.toggleCategoryActive(id);
        return ResponseEntity.ok(ApiResponse.success("Category active status updated", toggled));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERATIONS_SUPERVISOR') or hasRole('CUSTOMER_SUPPORT_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"));
    }
}
