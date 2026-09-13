package com.customersupport.SupportHUB.auth;

import com.customersupport.SupportHUB.agent.SupportAgent;
import com.customersupport.SupportHUB.agent.SupportAgentRepository;
import com.customersupport.SupportHUB.common.BadRequestException;
import com.customersupport.SupportHUB.common.DuplicateResourceException;
import com.customersupport.SupportHUB.common.JwtUtils;
import com.customersupport.SupportHUB.common.PasswordResetToken;
import com.customersupport.SupportHUB.common.PasswordResetTokenRepository;
import com.customersupport.SupportHUB.common.ResourceNotFoundException;
import com.customersupport.SupportHUB.common.Role;
import com.customersupport.SupportHUB.common.User;
import com.customersupport.SupportHUB.common.UserPrincipal;
import com.customersupport.SupportHUB.common.UserRepository;
import com.customersupport.SupportHUB.customer.Customer;
import com.customersupport.SupportHUB.customer.CustomerRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final SupportAgentRepository agentRepository;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthServiceImpl(
            UserRepository userRepository,
            CustomerRepository customerRepository,
            SupportAgentRepository agentRepository,
            PasswordResetTokenRepository resetTokenRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.agentRepository = agentRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    @Transactional
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered: " + request.getEmail());
        }

        Role role = request.getRole() != null ? request.getRole() : Role.CUSTOMER;

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setActive(true);
        user = userRepository.save(user);

        String fullName = request.getFullName();

        if (role == Role.CUSTOMER) {
            Customer customer = new Customer(user, request.getFullName(), request.getPhone(), request.getAddress());
            customerRepository.save(customer);
        } else if (role == Role.CUSTOMER_SERVICE_OFFICER || role == Role.OPERATIONS_SUPERVISOR || role == Role.QA_EXECUTIVE || role == Role.CUSTOMER_SUPPORT_MANAGER) {
            String employeeCode = (role == Role.CUSTOMER_SERVICE_OFFICER ? "AGT-" : "EMP-") + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            SupportAgent agent = new SupportAgent(user, request.getFullName(), request.getPhone(), employeeCode);
            agentRepository.save(agent);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new JwtResponse(jwt, user.getId(), user.getEmail(), user.getRole(), fullName);
    }

    @Override
    @Transactional
    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String fullName = user.getEmail();
        if (user.getRole() == Role.CUSTOMER) {
            fullName = customerRepository.findByUserId(user.getId())
                    .map(Customer::getFullName).orElse(user.getEmail());
        } else if (user.getRole() == Role.CUSTOMER_SERVICE_OFFICER) {
            fullName = agentRepository.findByUserId(user.getId())
                    .map(SupportAgent::getFullName).orElse(user.getEmail());
        }

        return new JwtResponse(jwt, user.getId(), user.getEmail(), user.getRole(), fullName);
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password does not match");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No user found with email: " + request.getEmail()));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user, LocalDateTime.now().plusHours(1));
        resetTokenRepository.save(resetToken);
        // In local academic setup, token is logged or stored for demonstration
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken token = resetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset token"));

        if (token.isUsed() || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Reset token is expired or has already been used");
        }

        User user = token.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        token.setUsed(true);
        resetTokenRepository.save(token);
    }
}
