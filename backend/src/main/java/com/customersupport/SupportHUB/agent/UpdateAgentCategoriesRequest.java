package com.customersupport.SupportHUB.agent;

import java.util.Set;

public class UpdateAgentCategoriesRequest {

    private Set<Long> categoryIds;

    public UpdateAgentCategoriesRequest() {
    }

    public UpdateAgentCategoriesRequest(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}

