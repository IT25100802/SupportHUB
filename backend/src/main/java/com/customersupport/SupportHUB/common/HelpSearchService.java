package com.customersupport.SupportHUB.common;

public interface HelpSearchService {
    HelpSearchResponse searchHelp(String query, Long categoryId);
    PlatformStatsDto getPlatformStats();
}
