package com.customersupport.SupportHUB.category;

import jakarta.validation.constraints.NotBlank;

public class CreateCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
    private String icon;
    private Long parentId;

    public CreateCategoryRequest() {
    }

    public CreateCategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CreateCategoryRequest(String name, String description, String icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public CreateCategoryRequest(String name, String description, String icon, Long parentId) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
