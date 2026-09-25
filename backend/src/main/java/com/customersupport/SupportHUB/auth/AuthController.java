package com.customersupport.SupportHUB.auth;

import com.customersupport.SupportHUB.common.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
        authService.registerCustomer(request);
        return ResponseEntity.ok(ApiResponse.success("Account created successfully. You can now sign in."));
    }

    @PostMapping("/register/customer")
    public ResponseEntity<ApiResponse<Void>> registerCustomerDirect(@Valid @RequestBody CustomerRegistrationRequest request) {
        authService.registerCustomer(request);
        return ResponseEntity.ok(ApiResponse.success("Account created successfully. You can now sign in."));
    }

    @PostMapping("/create-staff")
    @PreAuthorize("hasRole('CUSTOMER_SUPPORT_MANAGER') or hasRole('OPERATIONS_SUPERVISOR')")
    public ResponseEntity<ApiResponse<JwtResponse>> createStaff(@Valid @RequestBody RegisterRequest request) {
        JwtResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Staff member onboarded successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset instructions generated successfully"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully"));
    }
}
