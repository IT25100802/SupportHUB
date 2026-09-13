package com.customersupport.SupportHUB.common;

import com.customersupport.SupportHUB.agent.AgentStatus;
import com.customersupport.SupportHUB.agent.SupportAgent;
import com.customersupport.SupportHUB.agent.SupportAgentRepository;
import com.customersupport.SupportHUB.category.TicketCategory;
import com.customersupport.SupportHUB.category.TicketCategoryRepository;
import com.customersupport.SupportHUB.customer.Customer;
import com.customersupport.SupportHUB.customer.CustomerRepository;
import com.customersupport.SupportHUB.feedback.FeedbackRepository;
import com.customersupport.SupportHUB.notification.NotificationRepository;
import com.customersupport.SupportHUB.ticket.TicketHistoryRepository;
import com.customersupport.SupportHUB.ticket.TicketReplyRepository;
import com.customersupport.SupportHUB.ticket.TicketRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final SupportAgentRepository agentRepository;
    private final TicketCategoryRepository categoryRepository;
    private final TicketRepository ticketRepository;
    private final TicketHistoryRepository ticketHistoryRepository;
    private final TicketReplyRepository ticketReplyRepository;
    private final AttachmentRepository attachmentRepository;
    private final FeedbackRepository feedbackRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           CustomerRepository customerRepository,
                           SupportAgentRepository agentRepository,
                           TicketCategoryRepository categoryRepository,
                           TicketRepository ticketRepository,
                           TicketHistoryRepository ticketHistoryRepository,
                           TicketReplyRepository ticketReplyRepository,
                           AttachmentRepository attachmentRepository,
                           FeedbackRepository feedbackRepository,
                           NotificationRepository notificationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.agentRepository = agentRepository;
        this.categoryRepository = categoryRepository;
        this.ticketRepository = ticketRepository;
        this.ticketHistoryRepository = ticketHistoryRepository;
        this.ticketReplyRepository = ticketReplyRepository;
        this.attachmentRepository = attachmentRepository;
        this.feedbackRepository = feedbackRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Purge child table records first to prevent foreign key constraint violations
        feedbackRepository.deleteAll();
        ticketHistoryRepository.deleteAll();
        ticketReplyRepository.deleteAll();
        attachmentRepository.deleteAll();
        notificationRepository.deleteAll();
        ticketRepository.deleteAll();

        // 1. Seed Categories if empty
        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(List.of(
                new TicketCategory("Technical Support", "Hardware, software, and API integration issues"),
                new TicketCategory("Billing & Invoicing", "Payment processing, invoices, and refund requests"),
                new TicketCategory("Product Information", "Product feature documentation and specs"),
                new TicketCategory("Complaints & Escalations", "Service level agreements and supervisor escalations"),
                new TicketCategory("Account Issues", "Password resets, security, and account lockouts")
            ));
        }

        List<TicketCategory> categories = categoryRepository.findAll();
        Set<TicketCategory> categorySet = new HashSet<>(categories);

        // Ensure Top Management & Stakeholder Accounts exist with exact requested credentials
        User managerUser = userRepository.findByEmail("manager@demo.com").orElseGet(() ->
            userRepository.save(new User("manager@demo.com", passwordEncoder.encode("Manager123!"), Role.CUSTOMER_SUPPORT_MANAGER))
        );
        managerUser.setPasswordHash(passwordEncoder.encode("Manager123!"));
        userRepository.save(managerUser);

        User qaUser = userRepository.findByEmail("qa@demo.com").orElseGet(() ->
            userRepository.save(new User("qa@demo.com", passwordEncoder.encode("QaExecutive123!"), Role.QA_EXECUTIVE))
        );
        qaUser.setPasswordHash(passwordEncoder.encode("QaExecutive123!"));
        userRepository.save(qaUser);

        User supervisorUser = userRepository.findByEmail("supervisor@demo.com").orElseGet(() ->
            userRepository.save(new User("supervisor@demo.com", passwordEncoder.encode("Supervisor123!"), Role.OPERATIONS_SUPERVISOR))
        );
        supervisorUser.setPasswordHash(passwordEncoder.encode("Supervisor123!"));
        userRepository.save(supervisorUser);

        User officerUser = userRepository.findByEmail("officer@demo.com").orElseGet(() ->
            userRepository.save(new User("officer@demo.com", passwordEncoder.encode("Officer123!"), Role.CUSTOMER_SERVICE_OFFICER))
        );
        officerUser.setPasswordHash(passwordEncoder.encode("Officer123!"));
        userRepository.save(officerUser);

        SupportAgent agent = agentRepository.findByEmployeeCode("AGT-1001").orElse(null);
        if (agent == null) {
            agent = new SupportAgent(officerUser, "AGT-1001", "Demo Support Officer", "+94 77 1126656");
            agent.setStatus(AgentStatus.AVAILABLE);
            agent.setAssignedCategories(categorySet);
            agentRepository.save(agent);
        } else {
            agent.setAssignedCategories(categorySet);
            agentRepository.save(agent);
        }

        // Ensure Primary Customer exists with exact requested credentials
        User customerUser = userRepository.findByEmail("customer@demo.com").orElseGet(() ->
            userRepository.save(new User("customer@demo.com", passwordEncoder.encode("Customer123!"), Role.CUSTOMER))
        );
        customerUser.setPasswordHash(passwordEncoder.encode("Customer123!"));
        userRepository.save(customerUser);

        customerRepository.findByUserEmail("customer@demo.com").orElseGet(() -> {
            return customerRepository.save(new Customer(customerUser, "John Doe", "+94 71 2345678", "Apex Tech Solutions"));
        });

        System.out.println(">>> SupportHUB Clean Startup Complete! Database reset with zero sample tickets.");
    }
}
