package com.customersupport.SupportHUB.auth;

public interface AuthService {
    void registerCustomer(CustomerRegistrationRequest request);
    JwtResponse register(RegisterRequest request);
    JwtResponse login(LoginRequest request);
    void changePassword(String email, ChangePasswordRequest request);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
}
