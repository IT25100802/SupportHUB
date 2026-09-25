package com.customersupport.SupportHUB.common;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Ensure ONLY Default OPERATIONS_SUPERVISOR Account exists for System Administration
        User supervisorUser = userRepository.findByEmail("supervisor@demo.com").orElseGet(() ->
            userRepository.save(new User("supervisor@demo.com", passwordEncoder.encode("Supervisor123!"), Role.OPERATIONS_SUPERVISOR))
        );
        supervisorUser.setPasswordHash(passwordEncoder.encode("Supervisor123!"));
        userRepository.save(supervisorUser);

        System.out.println(">>> SupportHUB Startup: Default Supervisor (supervisor@demo.com) verified.");
    }
}
