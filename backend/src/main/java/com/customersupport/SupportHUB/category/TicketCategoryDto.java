package com.customersupport.SupportHUB.category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketCategoryDto {

    private Long id;
    private String name;
    private String description;
    private String icon;
    private Long parentId;
    private String parentName;
    private List<TicketCategoryDto> subcategories = new ArrayList<>();
    private int subcategoryCount;
    private long activeTicketCount;
    private int officerCount;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TicketCategoryDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<TicketCategoryDto> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<TicketCategoryDto> subcategories) {
        this.subcategories = subcategories != null ? subcategories : new ArrayList<>();
    }

    public int getSubcategoryCount() {
        return subcategoryCount;
    }

    public void setSubcategoryCount(int subcategoryCount) {
        this.subcategoryCount = subcategoryCount;
    }

    public long getActiveTicketCount() {
        return activeTicketCount;
    }

    public void setActiveTicketCount(long activeTicketCount) {
        this.activeTicketCount = activeTicketCount;
    }

    public int getOfficerCount() {
        return officerCount;
    }

    public void setOfficerCount(int officerCount) {
        this.officerCount = officerCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
