package com.customersupport.SupportHUB.feedback;

import com.customersupport.SupportHUB.common.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<DashboardReportDto>> getDashboardReport() {
        DashboardReportDto report = reportService.getDashboardReport();
        return ResponseEntity.ok(ApiResponse.success("Dashboard metrics fetched successfully", report));
    }

    @GetMapping("/feedback")
    @PreAuthorize("hasAnyRole('QA_EXECUTIVE', 'CUSTOMER_SUPPORT_MANAGER', 'OPERATIONS_SUPERVISOR')")
    public ResponseEntity<ApiResponse<FeedbackReportDto>> getFeedbackReport() {
        FeedbackReportDto report = reportService.getFeedbackReport();
        return ResponseEntity.ok(ApiResponse.success("Feedback CSAT report fetched successfully", report));
    }
}
