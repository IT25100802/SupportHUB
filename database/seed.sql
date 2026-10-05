-- =====================================================================================
-- SupportHUB Customer Support System — Comprehensive Enterprise Seed Dataset
-- Target Database : MySQL 8.0+ / Spring Boot 3+ JPA / Hibernate
-- Default Password for ALL Seed Accounts: Supervisor123!
-- BCrypt Hash     : $2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy
-- =====================================================================================

USE customer_support_db;

-- -------------------------------------------------------------------------------------
-- 0. Safely Clear Any Existing Data (Reset Foreign Key Checks)
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
-- 1. USERS TABLE
-- Roles: OPERATIONS_SUPERVISOR, CUSTOMER_SUPPORT_MANAGER, QA_EXECUTIVE, CUSTOMER_SERVICE_OFFICER, CUSTOMER
-- -------------------------------------------------------------------------------------
INSERT INTO users (id, email, password_hash, role, is_active, last_login_at, created_at, updated_at) VALUES
-- Admin / Management Staff
(1, 'supervisor@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'OPERATIONS_SUPERVISOR', 1, '2026-10-05 08:30:00', '2026-09-01 08:00:00', '2026-10-05 08:30:00'),
(2, 'manager@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER_SUPPORT_MANAGER', 1, '2026-10-05 09:00:00', '2026-09-01 08:00:00', '2026-10-05 09:00:00'),
(3, 'qa@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'QA_EXECUTIVE', 1, '2026-10-05 09:15:00', '2026-09-01 08:00:00', '2026-10-05 09:15:00'),

-- Customer Service Officers (Support Agents)
(4, 'agent.nadeesha@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER_SERVICE_OFFICER', 1, '2026-10-05 08:45:00', '2026-09-01 08:00:00', '2026-10-05 08:45:00'),
(5, 'agent.kasun@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER_SERVICE_OFFICER', 1, '2026-10-05 08:50:00', '2026-09-01 08:00:00', '2026-10-05 08:50:00'),
(6, 'agent.chamari@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER_SERVICE_OFFICER', 1, '2026-10-05 09:05:00', '2026-09-01 08:00:00', '2026-10-05 09:05:00'),
(7, 'agent.rohan@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER_SERVICE_OFFICER', 1, '2026-10-05 09:10:00', '2026-09-01 08:00:00', '2026-10-05 09:10:00'),

-- Customer Accounts
(8, 'customer.nimal@gmail.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER', 1, '2026-10-05 09:20:00', '2026-09-10 10:00:00', '2026-10-05 09:20:00'),
(9, 'customer.sanduni@gmail.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER', 1, '2026-10-04 17:30:00', '2026-09-12 11:30:00', '2026-10-04 17:30:00'),
(10, 'customer.dinesh@gmail.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER', 1, '2026-10-05 07:15:00', '2026-09-15 14:00:00', '2026-10-05 07:15:00'),
(11, 'customer.anusha@gmail.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER', 1, '2026-10-03 16:45:00', '2026-09-18 09:15:00', '2026-10-03 16:45:00'),
(12, 'customer.malik@gmail.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER', 1, '2026-10-05 10:00:00', '2026-09-20 13:00:00', '2026-10-05 10:00:00'),
(13, 'customer.tharindu@gmail.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER', 1, '2026-10-02 12:00:00', '2026-09-22 15:40:00', '2026-10-02 12:00:00'),
(14, 'customer.kavindi@gmail.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER', 1, '2026-10-04 18:20:00', '2026-09-25 10:20:00', '2026-10-04 18:20:00'),
(15, 'customer.roshini@gmail.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'CUSTOMER', 1, '2026-10-05 08:10:00', '2026-09-28 11:00:00', '2026-10-05 08:10:00');

-- -------------------------------------------------------------------------------------
-- 2. CUSTOMERS TABLE
-- -------------------------------------------------------------------------------------
INSERT INTO customers (id, user_id, full_name, phone, address, total_orders, created_at, updated_at) VALUES
(1, 8, 'Nimal Silva', '+94 77 123 4567', '45 Galle Road, Colombo 03', 5, '2026-09-10 10:00:00', '2026-10-05 09:20:00'),
(2, 9, 'Sanduni Jayasinghe', '+94 71 234 5678', '12 Peradeniya Road, Kandy', 3, '2026-09-12 11:30:00', '2026-10-04 17:30:00'),
(3, 10, 'Dinesh Rathnayake', '+94 76 345 6789', '88 Main Street, Galle', 8, '2026-09-15 14:00:00', '2026-10-05 07:15:00'),
(4, 11, 'Anusha Wickramasinghe', '+94 70 456 7890', '24 Kandy Road, Gampaha', 2, '2026-09-18 09:15:00', '2026-10-03 16:45:00'),
(5, 12, 'Malik Farook', '+94 72 567 8901', '105 Sea Street, Negombo', 6, '2026-09-20 13:00:00', '2026-10-05 10:00:00'),
(6, 13, 'Tharindu Bandara', '+94 78 678 9012', '56 Circular Road, Kurunegala', 4, '2026-09-22 15:40:00', '2026-10-02 12:00:00'),
(7, 14, 'Kavindi Perera', '+94 75 789 0123', '31 Beach Road, Matara', 1, '2026-09-25 10:20:00', '2026-10-04 18:20:00'),
(8, 15, 'Roshini De Silva', '+94 77 890 1234', '77 Havelock Road, Colombo 05', 7, '2026-09-28 11:00:00', '2026-10-05 08:10:00');

-- -------------------------------------------------------------------------------------
-- 3. CUSTOMER ORDERS TABLE
-- -------------------------------------------------------------------------------------
INSERT INTO customer_orders (id, customer_id, order_number, order_status, total_amount, items_count, order_date, created_at, updated_at) VALUES
(1, 1, 'ORD-2026-8801', 'COMPLETED', 14500.00, 2, '2026-09-12 14:30:00', '2026-09-12 14:30:00', '2026-09-12 14:30:00'),
(2, 1, 'ORD-2026-8802', 'COMPLETED', 8900.00, 1, '2026-09-28 10:15:00', '2026-09-28 10:15:00', '2026-09-28 10:15:00'),
(3, 2, 'ORD-2026-7701', 'COMPLETED', 22500.00, 3, '2026-09-15 16:00:00', '2026-09-15 16:00:00', '2026-09-15 16:00:00'),
(4, 2, 'ORD-2026-7702', 'PROCESSING', 6400.00, 1, '2026-10-03 09:20:00', '2026-10-03 09:20:00', '2026-10-03 09:20:00'),
(5, 3, 'ORD-2026-6601', 'COMPLETED', 45000.00, 4, '2026-09-18 11:45:00', '2026-09-18 11:45:00', '2026-09-18 11:45:00'),
(6, 3, 'ORD-2026-6602', 'RETURNED', 12800.00, 1, '2026-09-25 13:10:00', '2026-09-25 13:10:00', '2026-09-25 13:10:00'),
(7, 4, 'ORD-2026-5501', 'COMPLETED', 17200.00, 2, '2026-09-20 15:30:00', '2026-09-20 15:30:00', '2026-09-20 15:30:00'),
(8, 5, 'ORD-2026-4401', 'COMPLETED', 34000.00, 2, '2026-09-22 09:50:00', '2026-09-22 09:50:00', '2026-09-22 09:50:00'),
(9, 5, 'ORD-2026-4402', 'DELIVERED', 5200.00, 1, '2026-10-01 14:00:00', '2026-10-01 14:00:00', '2026-10-01 14:00:00'),
(10, 6, 'ORD-2026-3301', 'COMPLETED', 9800.00, 1, '2026-09-24 17:15:00', '2026-09-24 17:15:00', '2026-09-24 17:15:00'),
(11, 7, 'ORD-2026-2201', 'PROCESSING', 19500.00, 3, '2026-10-02 11:00:00', '2026-10-02 11:00:00', '2026-10-02 11:00:00'),
(12, 8, 'ORD-2026-1101', 'COMPLETED', 68000.00, 5, '2026-09-29 10:30:00', '2026-09-29 10:30:00', '2026-09-29 10:30:00'),
(13, 8, 'ORD-2026-1102', 'DELIVERED', 11200.00, 1, '2026-10-04 16:40:00', '2026-10-04 16:40:00', '2026-10-04 16:40:00');

-- -------------------------------------------------------------------------------------
-- 4. TICKET CATEGORIES TABLE
-- -------------------------------------------------------------------------------------
INSERT INTO ticket_categories (id, name, description, icon, is_active, parent_id, created_at, updated_at) VALUES
(1, 'Delivery & Shipping', 'Courier delays, package tracking, wrong delivery address, missing parcels', 'fa-truck', 1, NULL, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(2, 'Payment & Refunds', 'Double billing, checkout payment failures, refund status, card transaction errors', 'fa-credit-card', 1, NULL, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(3, 'Order Issues', 'Damaged items, incorrect size or color, missing accessories, order cancellation', 'fa-box', 1, NULL, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(4, 'Account & Security', 'Login issues, password reset, 2FA authorization, profile updates, privacy inquiries', 'fa-user-shield', 1, NULL, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(5, 'Technical & App Issues', 'Mobile app crashes, website loading bugs, promo code errors, checkout glitches', 'fa-laptop-code', 1, NULL, '2026-09-01 08:00:00', '2026-09-01 08:00:00'),
(6, 'Product Warranty & Returns', 'Manufacturer warranty claims, replacement requests, return policy guidance', 'fa-rotate-left', 1, NULL, '2026-09-01 08:00:00', '2026-09-01 08:00:00');

-- -------------------------------------------------------------------------------------
-- 5. SUPPORT AGENTS TABLE
-- -------------------------------------------------------------------------------------
INSERT INTO support_agents (id, user_id, full_name, phone, employee_code, status, created_at, updated_at) VALUES
(1, 4, 'Nadeesha Fernando', '+94 77 901 2345', 'AGT-1001', 'AVAILABLE', '2026-09-01 08:00:00', '2026-10-05 08:45:00'),
(2, 5, 'Kasun Perera', '+94 71 890 1234', 'AGT-1002', 'AVAILABLE', '2026-09-01 08:00:00', '2026-10-05 08:50:00'),
(3, 6, 'Chamari Alwis', '+94 76 789 0123', 'AGT-1003', 'AVAILABLE', '2026-09-01 08:00:00', '2026-10-05 09:05:00'),
(4, 7, 'Rohan Jayawardena', '+94 70 678 9012', 'AGT-1004', 'BUSY', '2026-09-01 08:00:00', '2026-10-05 09:10:00');

-- -------------------------------------------------------------------------------------
-- 6. SUPPORT AGENT CATEGORIES MAPPING (Many-to-Many)
-- -------------------------------------------------------------------------------------
INSERT INTO support_agent_categories (agent_id, category_id) VALUES
(1, 1), (1, 3), -- Nadeesha handles Delivery & Order Issues
(2, 2), (2, 4), -- Kasun handles Payment & Account Security
(3, 1), (3, 3), (3, 6), -- Chamari handles Delivery, Order Issues, Warranty
(4, 5), (4, 6), (4, 2); -- Rohan handles Technical, Warranty, Payments

-- -------------------------------------------------------------------------------------
-- 7. TICKETS TABLE (25+ Realistic Real-World Enterprise Tickets)
-- Priorities : LOW, MEDIUM, HIGH, URGENT
-- Statuses   : OPEN, IN_PROGRESS, RESOLVED, CLOSED
-- Refunds    : NONE, REQUESTED, PROCESSING, APPROVED, REJECTED, REFUNDED
-- -------------------------------------------------------------------------------------
INSERT INTO tickets (id, ticket_number, customer_id, category_id, assigned_agent_id, subject, description, priority, status, order_number, tracking_number, refund_status, sla_due_at, is_escalated, created_at, updated_at, resolved_at, closed_at) VALUES
-- Resolved & Closed Tickets (with full discussion and customer ratings)
(1, 'TKT-2026-000001', 1, 3, 1, 'Wrong T-Shirt Size Delivered in Package', 'I ordered a Men Large (L) slim-fit cotton shirt under order ORD-2026-8801, but received Small (S). Please exchange or refund.', 'MEDIUM', 'RESOLVED', 'ORD-2026-8801', 'TRK-LK-994821', 'APPROVED', '2026-09-15 14:00:00', 0, '2026-09-13 09:30:00', '2026-09-14 16:20:00', '2026-09-14 16:20:00', NULL),
(2, 'TKT-2026-000002', 2, 2, 2, 'Credit Card Charged Twice on Checkout', 'During payment checkout for order ORD-2026-7701, the gateway timed out. I retried and got charged LKR 22,500 twice on my Commercial Bank Visa card.', 'HIGH', 'RESOLVED', 'ORD-2026-7701', NULL, 'REFUNDED', '2026-09-17 16:00:00', 0, '2026-09-16 10:15:00', '2026-09-17 11:30:00', '2026-09-17 11:30:00', NULL),
(3, 'TKT-2026-000003', 3, 1, 3, 'Courier Package Delayed Over 5 Days', 'My package ORD-2026-6601 with tracking TRK-LK-883912 has been stuck in the Kandy sorting hub since Monday with no delivery attempts.', 'HIGH', 'CLOSED', 'ORD-2026-6601', 'TRK-LK-883912', 'NONE', '2026-09-20 18:00:00', 0, '2026-09-19 11:00:00', '2026-09-21 15:45:00', '2026-09-21 14:00:00', '2026-09-21 15:45:00'),
(4, 'TKT-2026-000004', 4, 6, 4, 'Electric Kettle Stopped Heating (Warranty Claim)', 'Purchased Philips Electric Kettle under order ORD-2026-5501. It stopped boiling water after 1 week. Please approve warranty service.', 'MEDIUM', 'RESOLVED', 'ORD-2026-5501', 'TRK-LK-774019', 'APPROVED', '2026-09-23 15:00:00', 0, '2026-09-21 13:40:00', '2026-09-22 17:10:00', '2026-09-22 17:10:00', NULL),
(5, 'TKT-2026-000005', 5, 4, 2, 'Unable to Receive 2FA SMS Verification Code', 'I changed my phone number and now cannot receive the 6-digit SMS verification code to access my profile.', 'HIGH', 'RESOLVED', NULL, NULL, 'NONE', '2026-09-24 12:00:00', 0, '2026-09-23 09:10:00', '2026-09-23 11:45:00', '2026-09-23 11:45:00', NULL),
(6, 'TKT-2026-000006', 6, 5, 4, 'Promo Code SUMMER25 Not Applying at Checkout', 'When entering code SUMMER25 at payment page, system throws invalid coupon error even though minimum purchase of 5,000 LKR is met.', 'LOW', 'CLOSED', 'ORD-2026-3301', NULL, 'NONE', '2026-09-26 17:00:00', 0, '2026-09-25 10:00:00', '2026-09-25 14:30:00', '2026-09-25 14:00:00', '2026-09-25 14:30:00'),
(7, 'TKT-2026-000007', 8, 3, 1, 'Missing Wireless Mouse from Laptop Bundle', 'Order ORD-2026-1101 arrived with laptop and bag, but the promotional Logitech wireless mouse was missing from the box.', 'MEDIUM', 'RESOLVED', 'ORD-2026-1101', 'TRK-LK-665120', 'NONE', '2026-10-01 10:00:00', 0, '2026-09-30 08:30:00', '2026-09-30 16:00:00', '2026-09-30 16:00:00', NULL),
(8, 'TKT-2026-000008', 3, 2, 2, 'Refund for Returned Blender ORD-2026-6602', 'I returned the damaged blender 3 days ago and courier delivered it back to your warehouse. When will refund reflect in my bank?', 'HIGH', 'RESOLVED', 'ORD-2026-6602', 'TRK-LK-554231', 'REFUNDED', '2026-09-30 18:00:00', 0, '2026-09-28 14:20:00', '2026-09-29 16:15:00', '2026-09-29 16:15:00', NULL),

-- In Progress Tickets (Active discussions)
(9, 'TKT-2026-000009', 1, 1, 1, 'Delivery Driver Refused to Deliver to 3rd Floor', 'Courier refused to bring package to my apartment floor and left. Need re-delivery scheduled for tomorrow morning.', 'MEDIUM', 'IN_PROGRESS', 'ORD-2026-8802', 'TRK-LK-443109', 'NONE', '2026-10-06 18:00:00', 0, '2026-10-04 11:15:00', '2026-10-05 09:30:00', NULL, NULL),
(10, 'TKT-2026-000010', 2, 1, 3, 'Change Shipping Address Before Dispatch', 'I entered wrong postal code on order ORD-2026-7702. Please change delivery address to 15 Temple Road, Peradeniya.', 'HIGH', 'IN_PROGRESS', 'ORD-2026-7702', NULL, 'NONE', '2026-10-05 16:00:00', 0, '2026-10-04 14:00:00', '2026-10-05 08:45:00', NULL, NULL),
(11, 'TKT-2026-000011', 5, 3, 1, 'Damaged Perfume Bottle with Broken Seal', 'Order ORD-2026-4402 arrived with broken glass bottle inside bubble wrap. Liquid leaked over the box.', 'HIGH', 'IN_PROGRESS', 'ORD-2026-4402', 'TRK-LK-332098', 'PROCESSING', '2026-10-06 12:00:00', 0, '2026-10-03 15:40:00', '2026-10-05 09:10:00', NULL, NULL),
(12, 'TKT-2026-000012', 7, 2, 2, 'Koko Pay 3-Month Installment Verification', 'Payment deducted first installment of 6,500 LKR but order status ORD-2026-2201 says Pending Payment.', 'URGENT', 'IN_PROGRESS', 'ORD-2026-2201', NULL, 'REQUESTED', '2026-10-05 15:00:00', 1, '2026-10-04 09:00:00', '2026-10-05 10:15:00', NULL, NULL),
(13, 'TKT-2026-000013', 8, 6, 4, 'Noise Cancelling Headphone Left Ear Cup Dead', 'Left speaker on Sony WH-1000XM4 produces static noise and no audio. Order ORD-2026-1102. Requesting replacement under warranty.', 'MEDIUM', 'IN_PROGRESS', 'ORD-2026-1102', 'TRK-LK-221087', 'NONE', '2026-10-07 14:00:00', 0, '2026-10-05 08:15:00', '2026-10-05 09:50:00', NULL, NULL),

-- New Open Tickets (Fresh incoming customer queries)
(14, 'TKT-2026-000014', 6, 1, 3, 'When will Order ORD-2026-3301 Arrive in Kurunegala?', 'Can you provide the courier tracking number and estimated delivery date for my shipment?', 'LOW', 'OPEN', 'ORD-2026-3301', NULL, 'NONE', '2026-10-07 18:00:00', 0, '2026-10-05 09:00:00', '2026-10-05 09:00:00', NULL, NULL),
(15, 'TKT-2026-000015', 4, 4, 2, 'Request Account Email Address Update', 'I lost access to my registered email address and want to update it to anusha.w@outlook.com with identity proof.', 'MEDIUM', 'OPEN', NULL, NULL, 'NONE', '2026-10-06 14:00:00', 0, '2026-10-05 09:25:00', '2026-10-05 09:25:00', NULL, NULL),
(16, 'TKT-2026-000016', 3, 5, 4, 'Mobile App Crashes on Android 14 Checkout', 'Whenever I click Proceed to Payment on the Android app version 2.4.1, the screen turns black and application crashes.', 'HIGH', 'OPEN', NULL, NULL, 'NONE', '2026-10-05 18:00:00', 0, '2026-10-05 09:40:00', '2026-10-05 09:40:00', NULL, NULL),
(17, 'TKT-2026-000017', 5, 2, 2, 'Store Credit Voucher Not Deducting from Total', 'I have 2,000 LKR wallet balance but checkout does not show the option to use store credits.', 'LOW', 'OPEN', NULL, NULL, 'NONE', '2026-10-08 12:00:00', 0, '2026-10-05 10:05:00', '2026-10-05 10:05:00', NULL, NULL),

-- SLA Breached Historical Tickets (For QA CSAT Analysis & Supervisor Management)
(18, 'TKT-2026-000018', 7, 1, 3, 'Package Lost in Transit for 2 Weeks', 'Tracking shows no movement for 14 days. Customer was kept waiting with no status updates.', 'URGENT', 'RESOLVED', 'ORD-2026-2201', 'TRK-LK-110976', 'APPROVED', '2026-09-28 12:00:00', 1, '2026-09-24 10:00:00', '2026-09-30 16:00:00', '2026-09-30 16:00:00', NULL),
(19, 'TKT-2026-000019', 4, 3, 1, 'Wrong Color Handbag Sent for Anniversary Gift', 'Ordered Champagne Gold but received Matte Black. Needed it urgently for an event and exchange took too long.', 'HIGH', 'RESOLVED', 'ORD-2026-5501', 'TRK-LK-990865', 'NONE', '2026-09-25 15:00:00', 1, '2026-09-22 11:30:00', '2026-09-27 10:20:00', '2026-09-27 10:20:00', NULL),
(20, 'TKT-2026-000020', 1, 6, 4, 'Smartwatch Battery Drain within 3 Hours', 'Brand new smartwatch does not last even half a day on full charge. Claiming replacement under manufacturer warranty.', 'MEDIUM', 'CLOSED', 'ORD-2026-8802', 'TRK-LK-880754', 'NONE', '2026-10-02 18:00:00', 0, '2026-09-29 09:15:00', '2026-10-02 14:00:00', '2026-10-01 17:30:00', '2026-10-02 14:00:00'),
(21, 'TKT-2026-000021', 2, 2, 2, 'Cancellation Request for Unshipped Order', 'Order was placed by mistake. Requested immediate cancellation and refund to original debit card.', 'LOW', 'CLOSED', 'ORD-2026-7701', NULL, 'REFUNDED', '2026-09-20 14:00:00', 0, '2026-09-18 12:10:00', '2026-09-19 15:30:00', '2026-09-19 14:00:00', '2026-09-19 15:30:00'),
(22, 'TKT-2026-000022', 8, 5, 4, 'Invoice PDF Download Generating 500 Server Error', 'Clicking Download Tax Invoice on the orders history page throws a server error instead of downloading PDF.', 'LOW', 'RESOLVED', 'ORD-2026-1101', NULL, 'NONE', '2026-10-04 18:00:00', 0, '2026-10-03 10:00:00', '2026-10-04 11:30:00', '2026-10-04 11:30:00', NULL),
(23, 'TKT-2026-000023', 5, 1, 3, 'Driver Demanded Extra Cash for Cash-On-Delivery', 'Courier agent asked for 500 LKR above invoice total claiming extra fuel surcharge. This is unacceptable.', 'URGENT', 'RESOLVED', 'ORD-2026-4401', 'TRK-LK-770643', 'NONE', '2026-09-26 12:00:00', 1, '2026-09-24 16:30:00', '2026-09-25 11:00:00', '2026-09-25 11:00:00', NULL),
(24, 'TKT-2026-000024', 6, 3, 1, 'Expired Beauty Products Received in Skincare Pack', 'Batch expiry date on lotion bottle is August 2026. Please replace with fresh batch from warehouse.', 'HIGH', 'RESOLVED', 'ORD-2026-3301', 'TRK-LK-660532', 'APPROVED', '2026-09-28 15:00:00', 0, '2026-09-26 13:20:00', '2026-09-27 16:45:00', '2026-09-27 16:45:00', NULL),
(25, 'TKT-2026-000025', 3, 4, 2, 'Unrecognized Login Attempt Notification Alert', 'I received a security email about a login from Singapore IP address. Need account secured immediately.', 'URGENT', 'RESOLVED', NULL, NULL, 'NONE', '2026-10-02 10:00:00', 0, '2026-10-01 20:30:00', '2026-10-02 08:45:00', '2026-10-02 08:45:00', NULL);

-- -------------------------------------------------------------------------------------
-- 8. TICKET REPLIES TABLE (Realistic Discussion Histories)
-- -------------------------------------------------------------------------------------
INSERT INTO ticket_replies (id, ticket_id, user_id, message, is_internal, created_at) VALUES
-- Ticket 1 Replies (Resolved)
(1, 1, 4, 'Hello Nimal, thank you for reaching out to SupportHUB. We apologize for delivering the wrong shirt size. We have authorized a replacement dispatch with courier pickup of the Small shirt.', 0, '2026-09-13 11:00:00'),
(2, 1, 1, 'Internal Note: Replacement order generated under #EXCH-8801. Courier collection scheduled.', 1, '2026-09-13 11:05:00'),
(3, 1, 8, 'Thank you Nadeesha! The courier arrived today and swapped the sizes. The Large fits perfectly now.', 0, '2026-09-14 15:30:00'),
(4, 1, 4, 'We are delighted to hear that! Marking this ticket as Resolved. Have a wonderful day.', 0, '2026-09-14 16:20:00'),

-- Ticket 2 Replies (Resolved)
(5, 2, 5, 'Dear Sanduni, we have verified the duplicate payment with our merchant bank gateway. The reversal of LKR 22,500 has been initiated back to your Commercial Bank account.', 0, '2026-09-16 14:20:00'),
(6, 2, 9, 'I received the SMS credit confirmation from my bank. Thank you so much for the swift help!', 0, '2026-09-17 11:15:00'),
(7, 2, 5, 'Glad we could resolve this quickly for you. Marking ticket as Resolved.', 0, '2026-09-17 11:30:00'),

-- Ticket 3 Replies (Closed)
(8, 3, 6, 'Hi Dinesh, we contacted the Kandy depot manager. Your package TRK-LK-883912 has been loaded onto the express van for delivery today before 3 PM.', 0, '2026-09-20 09:30:00'),
(9, 3, 10, 'Package was delivered at 2 PM. Goods are in good condition.', 0, '2026-09-21 14:10:00'),
(10, 3, 6, 'Thank you for confirming delivery! Closing this ticket.', 0, '2026-09-21 15:45:00'),

-- Ticket 4 Replies (Resolved)
(11, 4, 7, 'Hello Anusha, please drop the electric kettle at any Singer Service Center quoting warranty reference #WAR-5501 for a free heating element replacement.', 0, '2026-09-22 10:15:00'),
(12, 4, 11, 'Got the brand new replacement unit from the service center today. Thanks for the quick support.', 0, '2026-09-22 16:50:00'),

-- Ticket 9 Replies (In Progress)
(13, 9, 4, 'Hello Nimal, we have instructed the courier supervisor to ensure door-to-door delivery to your 3rd floor apartment tomorrow between 9 AM - 12 PM.', 0, '2026-10-04 15:00:00'),
(14, 9, 8, 'Understood. I will be available at home tomorrow morning. Please ensure driver calls before arriving.', 0, '2026-10-05 09:30:00'),

-- Ticket 10 Replies (In Progress)
(15, 10, 6, 'Hello Sanduni, the dispatch team has updated the shipping destination address to 15 Temple Road, Peradeniya.', 0, '2026-10-05 08:45:00'),

-- Ticket 11 Replies (In Progress)
(16, 11, 4, 'Hello Malik, we are extremely sorry for the damaged perfume bottle. We have initiated a full refund of LKR 5,200.', 0, '2026-10-04 10:20:00'),
(17, 11, 12, 'Thank you. Uploaded the photo of the broken bottle and leaked box for your inspection records.', 0, '2026-10-05 09:10:00');

-- -------------------------------------------------------------------------------------
-- 9. TICKET HISTORY TABLE (Audit Trail Log)
-- -------------------------------------------------------------------------------------
INSERT INTO ticket_history (id, ticket_id, performed_by_id, action, old_value, new_value, timestamp) VALUES
(1, 1, 8, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', '2026-09-13 09:30:00'),
(2, 1, 1, 'TICKET_ASSIGNED', 'Unassigned', 'Assigned to Nadeesha Fernando (AGT-1001)', '2026-09-13 09:35:00'),
(3, 1, 4, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', '2026-09-13 11:00:00'),
(4, 1, 4, 'REFUND_STATUS_CHANGED', 'NONE', 'APPROVED', '2026-09-14 14:00:00'),
(5, 1, 4, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', '2026-09-14 16:20:00'),

(6, 2, 9, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', '2026-09-16 10:15:00'),
(7, 2, 1, 'TICKET_ASSIGNED', 'Unassigned', 'Assigned to Kasun Perera (AGT-1002)', '2026-09-16 10:20:00'),
(8, 2, 5, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', '2026-09-16 14:20:00'),
(9, 2, 5, 'REFUND_STATUS_CHANGED', 'NONE', 'REFUNDED', '2026-09-17 11:00:00'),
(10, 2, 5, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', '2026-09-17 11:30:00'),

(11, 3, 10, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', '2026-09-19 11:00:00'),
(12, 3, 1, 'TICKET_ASSIGNED', 'Unassigned', 'Assigned to Chamari Alwis (AGT-1003)', '2026-09-19 11:05:00'),
(13, 3, 6, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', '2026-09-20 09:30:00'),
(14, 3, 6, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', '2026-09-21 14:00:00'),
(15, 3, 6, 'STATUS_CHANGED', 'RESOLVED', 'CLOSED', '2026-09-21 15:45:00'),

(16, 11, 12, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', '2026-10-03 15:40:00'),
(17, 11, 1, 'TICKET_ASSIGNED', 'Unassigned', 'Assigned to Nadeesha Fernando (AGT-1001)', '2026-10-03 15:45:00'),
(18, 11, 4, 'REFUND_STATUS_CHANGED', 'NONE', 'PROCESSING', '2026-10-04 10:20:00'),
(19, 11, 4, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', '2026-10-04 10:20:00'),

(20, 12, 14, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: URGENT', '2026-10-04 09:00:00'),
(21, 12, 1, 'TICKET_ASSIGNED', 'Unassigned', 'Assigned to Kasun Perera (AGT-1002)', '2026-10-04 09:10:00'),
(22, 12, 1, 'SLA_ALERT', 'NORMAL', 'ESCALATED to Manager due to payment gateway delay', '2026-10-05 08:30:00');

-- -------------------------------------------------------------------------------------
-- 10. ATTACHMENTS TABLE
-- -------------------------------------------------------------------------------------
INSERT INTO attachments (id, ticket_id, reply_id, original_file_name, stored_file_name, file_type, file_size, file_path, uploaded_at) VALUES
(1, 1, NULL, 'shirt_size_tag_photo.jpg', 'attach_17911001_shirt_size.jpg', 'image/jpeg', 245000, '/uploads/tickets/attach_17911001_shirt_size.jpg', '2026-09-13 09:30:00'),
(2, 2, NULL, 'bank_statement_duplicate_charge.pdf', 'attach_17911002_bank_statement.pdf', 'application/pdf', 512000, '/uploads/tickets/attach_17911002_bank_statement.pdf', '2026-09-16 10:15:00'),
(3, 4, NULL, 'kettle_warranty_card_receipt.png', 'attach_17911004_warranty_card.png', 'image/png', 380000, '/uploads/tickets/attach_17911004_warranty_card.png', '2026-09-21 13:40:00'),
(4, 11, 17, 'broken_perfume_bottle_photo.jpg', 'attach_17911011_broken_bottle.jpg', 'image/jpeg', 420000, '/uploads/tickets/attach_17911011_broken_bottle.jpg', '2026-10-05 09:10:00');

-- -------------------------------------------------------------------------------------
-- 11. FEEDBACK TABLE (CSAT Ratings 1 to 5 Stars Feeding QA & Satisfaction Dashboard)
-- -------------------------------------------------------------------------------------
INSERT INTO feedback (id, ticket_id, customer_id, rating, comment, suggestions, created_at) VALUES
(1, 1, 1, 5, 'Superb service! The agent Nadeesha handled the size exchange within 24 hours. Very courteous and polite.', 'Keep up the fast doorstep courier exchange option.', '2026-09-14 16:30:00'),
(2, 2, 2, 5, 'Kasun from payment support resolved my double billing issue promptly and bank credit came fast.', 'Notify via WhatsApp in addition to SMS.', '2026-09-17 12:00:00'),
(3, 3, 3, 4, 'Delivery was late by 5 days initially, but officer Chamari followed up with courier and got it delivered.', 'Improve courier partner tracking accuracy.', '2026-09-21 16:00:00'),
(4, 4, 4, 5, 'Smooth warranty approval. Singer service center accepted the reference code without hassle.', 'Add a warranty claim tracker on mobile app.', '2026-09-22 17:30:00'),
(5, 5, 5, 5, 'Officer helped verify my identity and updated my mobile number for 2FA in under 20 minutes.', 'Add email backup 2FA options.', '2026-09-23 12:15:00'),
(6, 6, 6, 4, 'Promo code issue was solved and agent explained minimum cart value terms clearly.', 'Make promo code terms more visible on checkout banner.', '2026-09-25 15:00:00'),
(7, 7, 8, 5, 'Logitech mouse was dispatched separately and received in 2 days. Excellent customer satisfaction!', 'Check bundle contents before sealing outer box.', '2026-09-30 16:30:00'),
(8, 8, 3, 5, 'Refund for returned blender reflected in full. Very reliable store.', 'None, everything was great.', '2026-09-29 17:00:00'),
(9, 18, 7, 1, 'Package was missing for two weeks and I had to follow up 4 times before action was taken.', 'Please train staff to proactively call customers when courier is delayed.', '2026-09-30 16:45:00'),
(10, 19, 4, 2, 'Received wrong handbag color and exchange arrived after my wedding anniversary.', 'Improve warehouse item scanning before shipping.', '2026-09-27 11:00:00'),
(11, 20, 1, 4, 'Watch replacement was arranged smoothly after battery issue was diagnosed.', 'Test smart devices before dispatch.', '2026-10-02 14:30:00'),
(12, 21, 2, 5, 'Cancelled accidental order and money was refunded the next business day.', 'Excellent support team response.', '2026-09-19 16:00:00'),
(13, 22, 8, 4, 'Invoice PDF generation was fixed after reporting the bug.', 'Provide automated monthly PDF invoice download.', '2026-10-04 12:00:00'),
(14, 23, 5, 2, 'Delivery courier demanded extra money. Officer handled it but it was a frustrating experience.', 'Audit contracted courier drivers strictly.', '2026-09-25 12:00:00');

-- -------------------------------------------------------------------------------------
-- 12. FAQ ARTICLES TABLE (Self-Service Knowledge Repository)
-- -------------------------------------------------------------------------------------
INSERT INTO faq_articles (id, question, answer, category_id, is_published, keywords, view_count, created_by_id, created_at, updated_at) VALUES
(1, 'How do I track my order delivery in real time?', 'You can track your parcel live by visiting the My Orders section in your profile or entering your TRK tracking number into our homepage tracker.', 1, 1, 'track order, delivery status, courier, parcel, where is my order', 342, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),
(2, 'What should I do if my payment was deducted but order failed?', 'If your card was charged but no order confirmation appeared, the bank hold usually auto-reverses within 24-48 hours. You can open a ticket with your transaction ID for instant manual verification.', 2, 1, 'payment failed, double charge, money deducted, refund, card error', 285, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),
(3, 'What is the return policy for damaged or incorrect items?', 'Items can be returned or exchanged within 14 days of receipt. The product must be unused with original packaging and tags intact. We provide free doorstep courier pickup.', 3, 1, 'return policy, exchange, damaged goods, refund, return window', 412, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),
(4, 'How do I reset my account password if I forgot it?', 'Click the Forgot Password link on the login page, enter your registered email address, and follow the secure 60-minute password reset link sent to your inbox.', 4, 1, 'forgot password, reset password, login help, unlock account', 198, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),
(5, 'How long does a bank card refund take to credit?', 'Once approved by our support team, refunds via Credit/Debit cards take 2-5 business days depending on your issuing bank. Store credit refunds are instant.', 2, 1, 'refund timeline, bank credit, how long refund takes, money back', 520, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),
(6, 'Can I change my delivery address after placing an order?', 'Yes, as long as your order status is Processing or Confirmed. Once the package is Dispatched with courier, address changes require opening an urgent support ticket.', 1, 1, 'change address, wrong delivery address, update shipping', 176, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),
(7, 'How do I claim warranty on electronics and appliances?', 'All electronic devices carry an official 1-year manufacturer warranty. Open a ticket under Product Warranty with your order invoice to receive an authorized service center voucher.', 6, 1, 'warranty claim, electronic repair, broken product, warranty service', 230, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),
(8, 'What payment methods are supported on SupportHUB?', 'We support Visa, MasterCard, Koko Pay 3-Month Installments, Mintpay, Commercial Bank Direct Pay, and Cash on Delivery (COD) for selected regions.', 2, 1, 'payment methods, credit card, koko pay, installments, cod', 310, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00');

-- -------------------------------------------------------------------------------------
-- 13. KNOWLEDGE BASE ARTICLES TABLE (Comprehensive Customer Help Guides)
-- -------------------------------------------------------------------------------------
INSERT INTO knowledge_base_articles (id, title, content, category_id, is_published, tags, view_count, created_by_id, created_at, updated_at) VALUES
(1, 'Complete Guide to Order Returns, Replacements & Doorstep Pickups', 
'### Overview
Our 14-Day Hassle-Free Return Guarantee ensures complete peace of mind when shopping.

### Step-by-Step Return Process:
1. **Initiate Request:** Navigate to *My Tickets* and click *Create New Ticket*. Select category *Order Issues* or *Product Warranty*.
2. **Attach Evidence:** Take clear photographs of the product condition, serial number tag, and outer packaging box.
3. **Courier Pickup:** Our logistics partner will arrive at your address within 48 hours to collect the item with pre-printed return labels.
4. **Resolution:** Choose between an instant replacement dispatch or 100% money-back refund to your original payment method.', 
3, 1, 'returns, replacements, doorstep pickup, refund process, damaged goods', 640, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),

(2, 'Understanding Service Level Agreements (SLA) & Resolution Timelines', 
'### SupportHUB SLA Commitment
We are committed to delivering swift, high-quality customer assistance:

- **Urgent Priority:** First response under **1 Hour**, Target Resolution **4 Hours**.
- **High Priority (Payment & Lost Parcels):** First response under **2 Hours**, Target Resolution **12 Hours**.
- **Medium Priority (Size & Color Exchanges):** First response under **4 Hours**, Target Resolution **24 Hours**.
- **Low Priority (General Inquiries):** Target Resolution **48 Hours**.

If any ticket approaches SLA breach, it is automatically escalated to our Operations Supervisor for immediate intervention.', 
4, 1, 'sla, response time, resolution deadline, escalation, customer guarantee', 490, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),

(3, 'How to Resolve Payment Failures, Gateway Timeouts & Double Deductions', 
'### Troubleshooting Payment Issues
If your transaction failed during online checkout:

1. **Check Bank SMS:** Verify whether funds were debited or placed on authorization hold.
2. **Authorization Holds:** If order was not confirmed, banks automatically release hold balances within 24 to 72 hours.
3. **Open a Payment Ticket:** Submit your 12-digit Bank Reference / RRN number in ticket details for instant clearance.', 
2, 1, 'payment gateway, double charge, bank reversal, checkout error, transaction ID', 580, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00'),

(4, 'Account Security Best Practices: Enabling 2FA & Password Management', 
'### Keeping Your SupportHUB Account Secure
1. **Strong Passwords:** Use a unique combination of uppercase letters, numbers, and special characters (!@#$%).
2. **Two-Factor Authentication (2FA):** Enable SMS verification in your Customer Profile settings.
3. **Recognize Phishing:** SupportHUB officers will never ask for your password or credit card CVV numbers.', 
4, 1, 'account security, 2fa, passwords, protect profile, safety guide', 320, 1, '2026-09-01 08:00:00', '2026-10-05 08:00:00');

-- -------------------------------------------------------------------------------------
-- 14. NOTIFICATIONS TABLE (Real-Time System Notifications)
-- -------------------------------------------------------------------------------------
INSERT INTO notifications (id, user_id, title, message, type, related_ticket_id, is_read, created_at) VALUES
(1, 8, 'Ticket Resolved: TKT-2026-000001', 'Your ticket regarding Wrong T-Shirt Size has been resolved. Please rate your experience.', 'TICKET_RESOLVED', 1, 1, '2026-09-14 16:20:00'),
(2, 4, 'New Feedback: TKT-2026-000001', 'Customer Nimal Silva submitted a 5-star rating for ticket TKT-2026-000001.', 'FEEDBACK_RECEIVED', 1, 0, '2026-09-14 16:30:00'),
(3, 9, 'Ticket Resolved: TKT-2026-000002', 'Refund of LKR 22,500 has been credited to your card. Ticket marked as Resolved.', 'TICKET_RESOLVED', 2, 1, '2026-09-17 11:30:00'),
(4, 5, 'New Feedback: TKT-2026-000002', 'Customer Sanduni Jayasinghe submitted a 5-star rating for ticket TKT-2026-000002.', 'FEEDBACK_RECEIVED', 2, 0, '2026-09-17 12:00:00'),
(5, 14, 'New Reply on Ticket: TKT-2026-000012', 'Officer Kasun Perera posted an update regarding your Koko Pay installment verification.', 'TICKET_REPLIED', 12, 0, '2026-10-05 10:15:00'),
(6, 1, '⚠️ SLA Breach Alert: TKT-2026-000018', 'Ticket TKT-2026-000018 for Package Lost in Transit has exceeded SLA deadline.', 'SLA_BREACH', 18, 0, '2026-09-28 12:05:00'),
(7, 3, 'QA Review Reminder', '14 new customer feedback reviews are ready for Quality Assurance inspection.', 'SYSTEM', NULL, 0, '2026-10-05 09:00:00');

-- -------------------------------------------------------------------------------------
-- 15. AUDIT LOGS TABLE (Platform Governance & Compliance Trail)
-- -------------------------------------------------------------------------------------
INSERT INTO audit_logs (id, user_id, action, entity_name, entity_id, details, timestamp) VALUES
(1, 1, 'USER_LOGIN', 'users', 1, 'Supervisor logged into SupportHUB Command Console', '2026-10-05 08:30:00'),
(2, 2, 'USER_LOGIN', 'users', 2, 'Manager logged in to review SLA metrics and team reports', '2026-10-05 09:00:00'),
(3, 3, 'USER_LOGIN', 'users', 3, 'QA Executive logged in for customer feedback review', '2026-10-05 09:15:00'),
(4, 1, 'CATEGORY_CREATE', 'ticket_categories', 1, 'Created system category Delivery & Shipping', '2026-09-01 08:00:00'),
(5, 1, 'CATEGORY_CREATE', 'ticket_categories', 2, 'Created system category Payment & Refunds', '2026-09-01 08:00:00'),
(6, 1, 'AGENT_ONBOARD', 'support_agents', 1, 'Onboarded Support Officer Nadeesha Fernando (AGT-1001)', '2026-09-01 08:00:00'),
(7, 1, 'AGENT_ONBOARD', 'support_agents', 2, 'Onboarded Support Officer Kasun Perera (AGT-1002)', '2026-09-01 08:00:00');
