package com.customersupport.SupportHUB.category;

import java.util.List;

public interface CategoryService {
    TicketCategoryDto createCategory(CreateCategoryRequest request);
    TicketCategoryDto getCategoryById(Long id);
    List<TicketCategoryDto> getAllCategories();
    List<TicketCategoryDto> getActiveCategories();
    TicketCategoryDto updateCategory(Long id, CreateCategoryRequest request);
    TicketCategoryDto toggleCategoryActive(Long id);
    void deleteCategory(Long id);
}
