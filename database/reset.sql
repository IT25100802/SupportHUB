-- SupportHUB Database Complete Full Wipe / Reset Script
-- Target Database: MySQL 8.0+
-- This script completely empties all 16 tables in customer_support_db

USE customer_support_db;

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
