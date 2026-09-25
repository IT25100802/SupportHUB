package com.customersupport.SupportHUB.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/help")
public class HelpSearchController {

    private final HelpSearchService helpSearchService;

    public HelpSearchController(HelpSearchService helpSearchService) {
        this.helpSearchService = helpSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<HelpSearchResponse>> search(
            @RequestParam(value = "q", required = false, defaultValue = "") String query,
            @RequestParam(value = "category", required = false) Long categoryId) {
        HelpSearchResponse response = helpSearchService.searchHelp(query, categoryId);
        return ResponseEntity.ok(ApiResponse.success("Help search completed", response));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<PlatformStatsDto>> getStats() {
        PlatformStatsDto stats = helpSearchService.getPlatformStats();
        return ResponseEntity.ok(ApiResponse.success("Platform statistics fetched", stats));
    }
}
