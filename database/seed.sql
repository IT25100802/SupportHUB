-- SE2030 Customer Support System Initial Seed Data (Core System Setup)
USE customer_support_db;

-- Clear existing data safely
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE audit_logs;
TRUNCATE TABLE password_reset_tokens;
TRUNCATE TABLE notifications;
TRUNCATE TABLE knowledge_base_articles;
TRUNCATE TABLE faq_articles;
TRUNCATE TABLE feedback;
TRUNCATE TABLE attachments;
TRUNCATE TABLE ticket_history;
TRUNCATE TABLE ticket_replies;
TRUNCATE TABLE tickets;
TRUNCATE TABLE support_agent_categories;
TRUNCATE TABLE support_agents;
TRUNCATE TABLE ticket_categories;
TRUNCATE TABLE customers;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Insert System Demo Role Accounts (BCrypt Hashed Passwords)
-- Customer123!     -> $2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK
-- Officer123!      -> $2a$10$ivVq4nbPjwjNp5XWU/c9.eXa.bJTtUCiPZ3G.DwG81OmyrtDTL0ne
-- Supervisor123!   -> $2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy
-- Manager123!      -> $2a$10$zNENUgBvFtPcH.t2NrYen.cYuNe24xohkFAxp0a0fxidAJNrSks.G
-- QaExecutive123!  -> $2a$10$Sp0LvQqIK6WvSxVdwMz0huXcm7LXI2xSSKI1jC217.2pD7gtQOqSe

INSERT INTO users (id, email, password_hash, role, is_active, created_at, updated_at) VALUES
(1, 'customer@demo.com', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, NOW(), NOW()),
(2, 'officer@demo.com', '$2a$10$ivVq4nbPjwjNp5XWU/c9.eXa.bJTtUCiPZ3G.DwG81OmyrtDTL0ne', 'CUSTOMER_SERVICE_OFFICER', 1, NOW(), NOW()),
(3, 'supervisor@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'OPERATIONS_SUPERVISOR', 1, NOW(), NOW()),
(4, 'manager@demo.com', '$2a$10$zNENUgBvFtPcH.t2NrYen.cYuNe24xohkFAxp0a0fxidAJNrSks.G', 'CUSTOMER_SUPPORT_MANAGER', 1, NOW(), NOW()),
(5, 'qa@demo.com', '$2a$10$Sp0LvQqIK6WvSxVdwMz0huXcm7LXI2xSSKI1jC217.2pD7gtQOqSe', 'QA_EXECUTIVE', 1, NOW(), NOW());

-- 2. Insert Base Customer Profile
INSERT INTO customers (id, user_id, full_name, phone, address, created_at, updated_at) VALUES
(1, 1, 'Demo Customer', '+94 71 2345678', '123 Innovation Way, Tech City', NOW(), NOW());

-- 3. Insert Base Ticket Categories
INSERT INTO ticket_categories (id, name, description, is_active, created_at, updated_at) VALUES
(1, 'Technical Support', 'Software glitches, system errors, integration & API issues', 1, NOW(), NOW()),
(2, 'Billing & Invoicing', 'Payment processing, subscription plans, refund requests', 1, NOW(), NOW()),
(3, 'Account Issues', 'Login problems, password resets, profile & 2FA configurations', 1, NOW(), NOW()),
(4, 'Product Information', 'General inquiries regarding features, hardware & software specs', 1, NOW(), NOW()),
(5, 'Complaints & Escalations', 'Formal complaints regarding service quality or delayed tickets', 1, NOW(), NOW());

-- 4. Insert Base Support Agent Profile
INSERT INTO support_agents (id, user_id, full_name, phone, employee_code, status, created_at, updated_at) VALUES
(1, 2, 'Demo Support Officer', '+94 77 1234567', 'AGT-1001', 'AVAILABLE', NOW(), NOW());

-- 5. Insert Support Agent Category Mappings
INSERT INTO support_agent_categories (agent_id, category_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);
