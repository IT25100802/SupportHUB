-- =====================================================================================
-- SupportHUB Customer Support System — Clean Initial Seed Dataset
-- Target Database : MySQL 8.0+ / Spring Boot 3+ JPA / Hibernate
-- Role            : Default Primary Supervisor Account Only (Zero Sample Categories/Data)
-- =====================================================================================

USE customer_support_db;

-- -------------------------------------------------------------------------------------
-- 0. Safely Clear Any Existing Data
-- -------------------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE audit_logs;
TRUNCATE TABLE password_reset_tokens;
TRUNCATE TABLE notifications;
TRUNCATE TABLE feedback;
TRUNCATE TABLE attachments;
TRUNCATE TABLE ticket_history;
TRUNCATE TABLE ticket_replies;
TRUNCATE TABLE tickets;
TRUNCATE TABLE support_agent_categories;
TRUNCATE TABLE support_agents;
TRUNCATE TABLE faq_articles;
TRUNCATE TABLE knowledge_base_articles;
TRUNCATE TABLE ticket_categories;
TRUNCATE TABLE customer_orders;
TRUNCATE TABLE customers;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- -------------------------------------------------------------------------------------
-- 1. USERS TABLE (Only Default Primary System Supervisor Account)
-- Supervisor Password: Supervisor123!
-- BCrypt Hash: $2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy
-- -------------------------------------------------------------------------------------
INSERT INTO users (id, email, password_hash, role, is_active, last_login_at, created_at, updated_at) VALUES
(1, 'supervisor@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'OPERATIONS_SUPERVISOR', 1, NOW(), NOW(), NOW());
