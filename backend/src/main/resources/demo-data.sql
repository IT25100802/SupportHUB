-- =====================================================================================
-- SupportHUB Customer Support System — Enterprise Demo Dataset
-- Target Database : MySQL 8.0+ / Spring Boot 3+ JPA / Hibernate
-- Consistency     : 100% Internally Consistent, Zero FK/Unique Violations, BCrypt Hashes
-- =====================================================================================

USE customer_support_db;

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
TRUNCATE TABLE customer_orders;
TRUNCATE TABLE support_agent_categories;
TRUNCATE TABLE support_agents;
TRUNCATE TABLE ticket_categories;
TRUNCATE TABLE customers;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. USERS
INSERT INTO users (id, email, password_hash, role, is_active, last_login_at, created_at, updated_at) VALUES
(1, 'customer@demo.com', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(2, 'officer@demo.com', '$2a$10$ivVq4nbPjwjNp5XWU/c9.eXa.bJTtUCiPZ3G.DwG81OmyrtDTL0ne', 'CUSTOMER_SERVICE_OFFICER', 1, DATE_SUB(NOW(), INTERVAL 20 MINUTE), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(3, 'supervisor@demo.com', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'OPERATIONS_SUPERVISOR', 1, DATE_SUB(NOW(), INTERVAL 15 MINUTE), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(4, 'manager@demo.com', '$2a$10$zNENUgBvFtPcH.t2NrYen.cYuNe24xohkFAxp0a0fxidAJNrSks.G', 'CUSTOMER_SUPPORT_MANAGER', 1, DATE_SUB(NOW(), INTERVAL 45 MINUTE), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(5, 'qa@demo.com', '$2a$10$Sp0LvQqIK6WvSxVdwMz0huXcm7LXI2xSSKI1jC217.2pD7gtQOqSe', 'QA_EXECUTIVE', 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(6, 'sarah.fernando@enterprise.lk', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),
(7, 'kasun.perera@techcorp.io', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 38 DAY), NOW()),
(8, 'dilani.silva@islandretail.com', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),
(9, 'rohan.gunasekara@logistics.lk', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 32 DAY), NOW()),
(10, 'anusha.wijesinghe@finserve.org', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 12 HOUR), DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(11, 'malik.de.silva@globalnexus.net', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY), NOW()),
(12, 'thilina.senaratne@urbanmart.lk', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 6 HOUR), DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(13, 'chamari.athukorala@primegoods.com', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(14, 'dinuka.jayasuriya@ceylondev.com', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 8 HOUR), DATE_SUB(NOW(), INTERVAL 22 DAY), NOW()),
(15, 'harith.mendis@apexsolutions.lk', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(16, 'nadeesha.wickramasinghe@quickshop.lk', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 18 HOUR), DATE_SUB(NOW(), INTERVAL 18 DAY), NOW()),
(17, 'pradeep.kumara@orientaldistributors.com', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), NOW()),
(18, 'sachini.rajapaksha@lankamed.org', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 5 HOUR), DATE_SUB(NOW(), INTERVAL 14 DAY), NOW()),
(19, 'buddhika.alwis@skylineholdings.lk', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), NOW()),
(20, 'oshadi.dissanayake@greenleaf.lk', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 7 HOUR), DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),
(21, 'kanishka.bandara@crestlogistics.com', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),
(22, 'shenali.cooray@infowave.lk', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 6 DAY), NOW()),
(23, 'tharindu.gamage@futurelabs.io', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
(24, 'sanduni.fonseka@silverline.lk', '$2a$10$t2aYSojOKnRdeUO06WsrUuG4da9P0Kn3pA3a0AXheEEQppeVzsbEK', 'CUSTOMER', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(25, 'nuwan.bandara@support.hub', '$2a$10$ivVq4nbPjwjNp5XWU/c9.eXa.bJTtUCiPZ3G.DwG81OmyrtDTL0ne', 'CUSTOMER_SERVICE_OFFICER', 1, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(26, 'kavindi.jayawardena@support.hub', '$2a$10$ivVq4nbPjwjNp5XWU/c9.eXa.bJTtUCiPZ3G.DwG81OmyrtDTL0ne', 'CUSTOMER_SERVICE_OFFICER', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(27, 'sajith.wickramasinghe@support.hub', '$2a$10$ivVq4nbPjwjNp5XWU/c9.eXa.bJTtUCiPZ3G.DwG81OmyrtDTL0ne', 'CUSTOMER_SERVICE_OFFICER', 1, DATE_SUB(NOW(), INTERVAL 4 HOUR), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(28, 'nimesha.rathnayake@support.hub', '$2a$10$ivVq4nbPjwjNp5XWU/c9.eXa.bJTtUCiPZ3G.DwG81OmyrtDTL0ne', 'CUSTOMER_SERVICE_OFFICER', 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(29, 'dharshana.abeykoon@support.hub', '$2a$10$ivVq4nbPjwjNp5XWU/c9.eXa.bJTtUCiPZ3G.DwG81OmyrtDTL0ne', 'CUSTOMER_SERVICE_OFFICER', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(30, 'ops.assistant@support.hub', '$2a$10$H0lLq2O4Z04TeIaIG5047u0vL9AorcXHgnm1wHCoCFW7YwzfQ5Wgy', 'OPERATIONS_SUPERVISOR', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(31, 'qa.auditor@support.hub', '$2a$10$Sp0LvQqIK6WvSxVdwMz0huXcm7LXI2xSSKI1jC217.2pD7gtQOqSe', 'QA_EXECUTIVE', 1, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 30 DAY), NOW());

-- 2. CUSTOMERS
INSERT INTO customers (id, user_id, full_name, phone, address, total_orders, created_at, updated_at) VALUES
(1, 1, 'John Doe', '+94 71 2345678', '123 Innovation Way, Tech City, Colombo 03', 3, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(2, 6, 'Sarah Fernando', '+94 77 3456789', '45 Galle Road, Bambalapitiya, Colombo 04', 4, DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),
(3, 7, 'Kasun Perera', '+94 76 4567890', '12 Kandy Road, Kiribathgoda, Kelaniya', 3, DATE_SUB(NOW(), INTERVAL 38 DAY), NOW()),
(4, 8, 'Dilani Silva', '+94 70 5678901', '78 Havelock Road, Colombo 05', 3, DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),
(5, 9, 'Rohan Gunasekara', '+94 72 6789012', '89 Peradeniya Road, Kandy', 2, DATE_SUB(NOW(), INTERVAL 32 DAY), NOW()),
(6, 10, 'Anusha Wijesinghe', '+94 75 7890123', '34 Marine Drive, Kollupitiya, Colombo 03', 3, DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(7, 11, 'Malik De Silva', '+94 78 8901234', '15 Negombo Road, Wattala', 2, DATE_SUB(NOW(), INTERVAL 28 DAY), NOW()),
(8, 12, 'Thilina Senaratne', '+94 71 9012345', '67 Matara Road, Galle Fort, Galle', 2, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(9, 13, 'Chamari Athukorala', '+94 77 0123456', '23 Main Street, Kurunegala', 2, DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(10, 14, 'Dinuka Jayasuriya', '+94 71 1234567', '56 High Level Road, Nugegoda', 2, DATE_SUB(NOW(), INTERVAL 22 DAY), NOW()),
(11, 15, 'Harith Mendis', '+94 76 2345678', '88 Stanley Thilakarathne Mawatha, Nugegoda', 2, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(12, 16, 'Nadeesha Wickramasinghe', '+94 77 8765432', '102 Ward Place, Colombo 07', 1, DATE_SUB(NOW(), INTERVAL 18 DAY), NOW()),
(13, 17, 'Pradeep Kumara', '+94 70 9876543', '14 Temple Road, Kalutara', 1, DATE_SUB(NOW(), INTERVAL 16 DAY), NOW()),
(14, 18, 'Sachini Rajapaksha', '+94 72 3456781', '72 Station Road, Dehiwala', 1, DATE_SUB(NOW(), INTERVAL 14 DAY), NOW()),
(15, 19, 'Buddhika Alwis', '+94 75 4567892', '91 Katugastota Road, Kandy', 1, DATE_SUB(NOW(), INTERVAL 12 DAY), NOW()),
(16, 20, 'Oshadi Dissanayake', '+94 78 5678903', '33 Badulla Road, Bandarawela', 1, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),
(17, 21, 'Kanishka Bandara', '+94 71 6789014', '18 Kurunegala Road, Anuradhapura', 1, DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),
(18, 22, 'Shenali Cooray', '+94 77 7890125', '42 Circular Road, Ratnapura', 1, DATE_SUB(NOW(), INTERVAL 6 DAY), NOW()),
(19, 23, 'Tharindu Gamage', '+94 76 8901236', '60 Dharmapala Mawatha, Colombo 03', 1, DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
(20, 24, 'Sanduni Fonseka', '+94 70 1122334', '85 Beach Road, Mount Lavinia', 1, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW());

-- 3. CUSTOMER_ORDERS
INSERT INTO customer_orders (id, customer_id, order_number, order_status, total_amount, items_count, order_date, created_at, updated_at) VALUES
(1, 1, 'ORD-98214', 'COMPLETED', 145.00, 2, DATE_SUB(NOW(), INTERVAL 28 DAY), DATE_SUB(NOW(), INTERVAL 28 DAY), NOW()),
(2, 1, 'ORD-98215', 'PROCESSING', 89.50, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(3, 1, 'ORD-98216', 'SHIPPED', 320.00, 3, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
(4, 2, 'ORD-98217', 'COMPLETED', 210.00, 4, DATE_SUB(NOW(), INTERVAL 24 DAY), DATE_SUB(NOW(), INTERVAL 24 DAY), NOW()),
(5, 2, 'ORD-98218', 'COMPLETED', 65.00, 1, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), NOW()),
(6, 2, 'ORD-98219', 'DELIVERED', 450.00, 5, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),
(7, 2, 'ORD-98220', 'CANCELLED', 110.00, 1, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),
(8, 3, 'ORD-98221', 'COMPLETED', 540.00, 2, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 22 DAY), NOW()),
(9, 3, 'ORD-98222', 'SHIPPED', 75.00, 1, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
(10, 3, 'ORD-98236', 'PROCESSING', 185.00, 2, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(11, 4, 'ORD-98223', 'COMPLETED', 180.00, 3, DATE_SUB(NOW(), INTERVAL 19 DAY), DATE_SUB(NOW(), INTERVAL 19 DAY), NOW()),
(12, 4, 'ORD-98224', 'PROCESSING', 95.00, 2, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(13, 4, 'ORD-98225', 'DELIVERED', 230.00, 2, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), NOW()),
(14, 5, 'ORD-98226', 'COMPLETED', 410.00, 4, DATE_SUB(NOW(), INTERVAL 17 DAY), DATE_SUB(NOW(), INTERVAL 17 DAY), NOW()),
(15, 5, 'ORD-98227', 'RETURNED', 160.00, 1, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), NOW()),
(16, 6, 'ORD-98228', 'COMPLETED', 340.00, 3, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), NOW()),
(17, 6, 'ORD-98229', 'SHIPPED', 125.00, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),
(18, 6, 'ORD-98230', 'COMPLETED', 520.00, 5, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NOW()),
(19, 7, 'ORD-98231', 'COMPLETED', 290.00, 2, DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 13 DAY), NOW()),
(20, 7, 'ORD-98232', 'PROCESSING', 85.00, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(21, 8, 'ORD-98233', 'COMPLETED', 175.00, 2, DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY), NOW()),
(22, 8, 'ORD-98234', 'DELIVERED', 380.00, 4, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), NOW()),
(23, 9, 'ORD-98235', 'COMPLETED', 215.00, 2, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), NOW()),
(24, 9, 'ORD-98237', 'SHIPPED', 95.00, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(25, 10, 'ORD-98238', 'COMPLETED', 140.00, 2, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 12 DAY), NOW()),
(26, 10, 'ORD-98239', 'PROCESSING', 220.00, 3, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),
(27, 11, 'ORD-98240', 'COMPLETED', 310.00, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),
(28, 11, 'ORD-98241', 'SHIPPED', 65.00, 1, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(29, 12, 'ORD-98242', 'COMPLETED', 190.00, 2, DATE_SUB(NOW(), INTERVAL 14 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), NOW()),
(30, 13, 'ORD-98243', 'DELIVERED', 275.00, 3, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), NOW()),
(31, 14, 'ORD-98244', 'COMPLETED', 85.00, 1, DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY), NOW()),
(32, 15, 'ORD-98245', 'PROCESSING', 420.00, 4, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(33, 16, 'ORD-98246', 'SHIPPED', 130.00, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),
(34, 17, 'ORD-98247', 'COMPLETED', 360.00, 3, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),
(35, 18, 'ORD-98248', 'DELIVERED', 95.00, 1, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), NOW());

-- 4. TICKET_CATEGORIES
INSERT INTO ticket_categories (id, name, description, icon, parent_id, is_active, created_at, updated_at) VALUES
(1, 'Order Issues', 'Handles order status, cancellations, missing items, wrong items, returns and exchanges.', 'fa-cart-shopping', NULL, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(2, 'Delivery Issues', 'Handles shipment delays, courier tracking errors, failed deliveries, and damaged transit packages.', 'fa-truck', NULL, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(3, 'Payment & Refunds', 'Handles payment gateway failures, duplicate charges, refund requests, and billing discrepancies.', 'fa-credit-card', NULL, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(4, 'Account Issues', 'Handles customer account security, login failures, password resets, 2FA, and profile updates.', 'fa-user', NULL, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(5, 'Product Information', 'Assistance regarding specifications, stock availability, warranty guidelines, and setup.', 'fa-box-open', NULL, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(6, 'Complaints & Escalations', 'Grievance handling, unresolved SLA breaches, and executive-level escalations.', 'fa-triangle-exclamation', NULL, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),

(11, 'Order Status & Tracking', 'Real-time order progression and fulfillment milestone tracking', 'fa-cart-shopping', 1, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(12, 'Order Cancellations', 'Requests to cancel orders prior to warehouse dispatch', 'fa-cart-shopping', 1, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(13, 'Missing or Wrong Items', 'Discrepancies in package contents, incorrect models, or missing accessories', 'fa-cart-shopping', 1, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(14, 'Returns & Exchanges', 'Return authorizations, product replacement, and exchange logistics', 'fa-cart-shopping', 1, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),

(21, 'Delayed Delivery', 'Shipments exceeding promised courier delivery SLAs', 'fa-truck', 2, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(22, 'Courier Tracking Problems', 'Missing waybill updates or incorrect checkpoint scans', 'fa-truck', 2, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(23, 'Failed Delivery Attempt', 'Courier rider unable to locate address or contact customer', 'fa-truck', 2, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(24, 'Damaged In-Transit Packages', 'Damaged exterior packaging, water damage, or compromised tamper seals', 'fa-truck', 2, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),

(31, 'Payment Gateway Failures', 'Card checkout connection timeouts and payment gateway declined errors', 'fa-credit-card', 3, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(32, 'Duplicate Charges', 'Multiple charges recorded on bank statement for single checkout', 'fa-credit-card', 3, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(33, 'Refund Request & Inquiries', 'Return credit processing and bank turnaround time tracking', 'fa-credit-card', 3, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(34, 'Invoice & Billing Issues', 'Tax invoices, commercial invoices and VAT receipts generation', 'fa-credit-card', 3, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),

(41, 'Login & Password Resets', 'Password recovery links, temporary access tokens, and lockout assistance', 'fa-user', 4, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(42, 'Profile & Address Updates', 'Updating registered contact details and primary delivery addresses', 'fa-user', 4, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(43, 'Account Verification', 'KYC compliance and customer identity verification checks', 'fa-user', 4, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(44, 'Two-Factor Authentication (2FA)', 'Authenticator app re-sync and emergency backup codes', 'fa-user', 4, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),

(51, 'Stock & Availability', 'Inventory replenishment queries and backorder estimations', 'fa-box-open', 5, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(52, 'Technical Specifications', 'Device compatibility, dimensions, and hardware performance metrics', 'fa-box-open', 5, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(53, 'Warranty Information', 'Manufacturer warranty terms, claims procedure, and service centers', 'fa-box-open', 5, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(54, 'Product Manuals & Guides', 'Online user guides, schematic diagrams, and assembly manuals', 'fa-box-open', 5, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),

(61, 'Service Quality & Staff Conduct', 'Feedback and grievances regarding customer support interaction quality', 'fa-triangle-exclamation', 6, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(62, 'Unresolved Ticket Complaints', 'Tickets exceeding SLA resolution windows without customer closure', 'fa-triangle-exclamation', 6, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(63, 'Executive Escalation Requests', 'Direct intervention by Operations Supervisor and Senior Management', 'fa-triangle-exclamation', 6, 1, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW());

-- 5. SUPPORT_AGENTS
INSERT INTO support_agents (id, user_id, full_name, phone, employee_code, status, created_at, updated_at) VALUES
(1, 2, 'Demo Support Officer', '+94 77 1234567', 'AGT-1001', 'AVAILABLE', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(2, 25, 'Nuwan Bandara', '+94 77 2345678', 'AGT-1002', 'AVAILABLE', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(3, 26, 'Kavindi Jayawardena', '+94 77 3456789', 'AGT-1003', 'BUSY', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(4, 27, 'Sajith Wickramasinghe', '+94 77 4567890', 'AGT-1004', 'AVAILABLE', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(5, 28, 'Nimesha Rathnayake', '+94 77 5678901', 'AGT-1005', 'BUSY', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(6, 29, 'Dharshana Abeykoon', '+94 77 6789012', 'AGT-1006', 'OFFLINE', DATE_SUB(NOW(), INTERVAL 45 DAY), NOW());

-- 6. SUPPORT_AGENT_CATEGORIES
INSERT INTO support_agent_categories (agent_id, category_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(2, 1), (2, 2), (2, 11), (2, 12), (2, 21), (2, 22),
(3, 3), (3, 31), (3, 32), (3, 33), (3, 34),
(4, 5), (4, 51), (4, 52), (4, 53), (4, 54),
(5, 4), (5, 6), (5, 41), (5, 42), (5, 61), (5, 62),
(6, 1), (6, 2), (6, 3), (6, 5);

-- 7. TICKETS
INSERT INTO tickets (id, ticket_number, customer_id, category_id, assigned_agent_id, subject, description, priority, status, order_number, tracking_number, refund_status, sla_due_at, is_escalated, created_at, updated_at, resolved_at, closed_at) VALUES
(1, 'TCK-2026-001', 1, 1, NULL, 'Package missing item in recent delivery', 'I received order ORD-98214 today but the wireless mouse accessory was missing from the parcel package.', 'MEDIUM', 'OPEN', 'ORD-98214', 'TRK-LK-88910', 'NONE', DATE_ADD(NOW(), INTERVAL 18 HOUR), 0, DATE_SUB(NOW(), INTERVAL 6 HOUR), NOW(), NULL, NULL),
(2, 'TCK-2026-002', 2, 3, NULL, 'Credit card charged twice during checkout', 'My bank statement indicates two identical charges of $110.00 for order ORD-98220 which failed at checkout.', 'HIGH', 'OPEN', 'ORD-98220', NULL, 'REQUESTED', DATE_ADD(NOW(), INTERVAL 8 HOUR), 0, DATE_SUB(NOW(), INTERVAL 4 HOUR), NOW(), NULL, NULL),
(3, 'TCK-2026-003', 3, 5, NULL, 'Inquiry regarding dual-monitor compatibility', 'I am planning to purchase the USB-C dock model Pro-X. Could you confirm if it supports dual 4K monitors on macOS?', 'LOW', 'OPEN', NULL, NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 36 HOUR), 0, DATE_SUB(NOW(), INTERVAL 8 HOUR), NOW(), NULL, NULL),
(4, 'TCK-2026-004', 6, 4, NULL, 'Unable to receive two-factor authentication SMS', 'I am trying to log in from a new work laptop but the 2FA SMS code is not arriving on my registered mobile phone.', 'HIGH', 'OPEN', NULL, NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 12 HOUR), 0, DATE_SUB(NOW(), INTERVAL 3 HOUR), NOW(), NULL, NULL),
(5, 'TCK-2026-005', 7, 2, 2, 'Courier tracking status stuck at Central Hub for 4 days', 'Order ORD-98232 has been showing Departure from Central Hub since Tuesday with no new milestone scan.', 'MEDIUM', 'OPEN', 'ORD-98232', 'TRK-LK-91204', 'NONE', DATE_ADD(NOW(), INTERVAL 24 HOUR), 0, DATE_SUB(NOW(), INTERVAL 5 HOUR), NOW(), NULL, NULL),
(6, 'TCK-2026-006', 10, 1, 1, 'Incorrect billing address printed on shipping label', 'The shipping label on ORD-98238 has our previous branch address. Need address correction before final dispatch.', 'LOW', 'OPEN', 'ORD-98238', NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 30 HOUR), 0, DATE_SUB(NOW(), INTERVAL 7 HOUR), NOW(), NULL, NULL),
(7, 'TCK-2026-007', 11, 3, 3, 'VAT exempt tax invoice request for government institution', 'Please generate tax invoice with exemption code LK-GOV-2026 for order ORD-98240.', 'LOW', 'OPEN', 'ORD-98240', NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 32 HOUR), 0, DATE_SUB(NOW(), INTERVAL 6 HOUR), NOW(), NULL, NULL),
(8, 'TCK-2026-008', 12, 5, 4, 'Stock availability inquiry for bulk laptop purchase', 'We are procuring 15 units of ThinkPad T14. Requesting availability confirmation and bulk discount quote.', 'MEDIUM', 'OPEN', NULL, NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 28 HOUR), 0, DATE_SUB(NOW(), INTERVAL 4 HOUR), NOW(), NULL, NULL),

(9, 'TCK-2026-009', 1, 2, 1, 'Expedited shipping request for urgent replacement', 'Need delivery confirmation for replacement unit under ORD-98215 before this Friday due to overseas travel.', 'HIGH', 'IN_PROGRESS', 'ORD-98215', 'TRK-LK-77412', 'NONE', DATE_ADD(NOW(), INTERVAL 4 HOUR), 0, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), NULL, NULL),
(10, 'TCK-2026-010', 4, 3, 3, 'Refund turnaround time inquiry for returned goods', 'Returned item for ORD-98223 was picked up by courier on Monday. Requesting confirmation on bank refund timeline.', 'MEDIUM', 'IN_PROGRESS', 'ORD-98223', 'TRK-LK-66521', 'PROCESSING', DATE_ADD(NOW(), INTERVAL 14 HOUR), 0, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), NULL, NULL),
(11, 'TCK-2026-011', 5, 2, 2, 'Damaged product packaging upon unboxing', 'The outer box of order ORD-98227 was crushed and the internal glass component is cracked.', 'HIGH', 'IN_PROGRESS', 'ORD-98227', 'TRK-LK-55410', 'REQUESTED', DATE_ADD(NOW(), INTERVAL 6 HOUR), 0, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), NULL, NULL),
(12, 'TCK-2026-012', 8, 4, 5, 'Update registered corporate billing email address', 'We have rebranded our domain and need to change account primary email from info@oldfirm.com to finance@urbanmart.lk.', 'LOW', 'IN_PROGRESS', NULL, NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 28 HOUR), 0, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), NULL, NULL),
(13, 'TCK-2026-013', 9, 5, 4, 'Firmware upgrade instructions for smart hub gateway', 'Need official documentation and download links for firmware patch v3.4.2 to resolve Wi-Fi disconnects.', 'MEDIUM', 'IN_PROGRESS', NULL, NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 20 HOUR), 0, DATE_SUB(NOW(), INTERVAL 18 HOUR), NOW(), NULL, NULL),
(14, 'TCK-2026-014', 2, 1, 1, 'Order cancellation request before warehouse dispatch', 'Please cancel order ORD-98219 as I selected the wrong color variant. I will place a new order immediately.', 'MEDIUM', 'IN_PROGRESS', 'ORD-98219', NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 8 HOUR), 0, DATE_SUB(NOW(), INTERVAL 12 HOUR), NOW(), NULL, NULL),
(15, 'TCK-2026-015', 13, 2, 6, 'Courier delivered package to wrong apartment block', 'Rider left package at Block B Security desk instead of Block D. Need driver to relocate parcel.', 'HIGH', 'IN_PROGRESS', 'ORD-98243', 'TRK-LK-66219', 'NONE', DATE_ADD(NOW(), INTERVAL 5 HOUR), 0, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), NULL, NULL),
(16, 'TCK-2026-016', 14, 3, 3, 'Partial discount voucher not applied at checkout', 'Promo coupon SPRING20 deducted only 5% instead of 20%. Requesting price adjustment of $17.00.', 'MEDIUM', 'IN_PROGRESS', 'ORD-98244', NULL, 'REQUESTED', DATE_ADD(NOW(), INTERVAL 16 HOUR), 0, DATE_SUB(NOW(), INTERVAL 1 DAY), NOW(), NULL, NULL),
(17, 'TCK-2026-017', 15, 1, 2, 'Change item size variant for pending order', 'Need to change shirt size from Medium to Extra Large for order ORD-98245 prior to packaging.', 'LOW', 'IN_PROGRESS', 'ORD-98245', NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 22 HOUR), 0, DATE_SUB(NOW(), INTERVAL 14 HOUR), NOW(), NULL, NULL),
(18, 'TCK-2026-018', 16, 4, 5, 'Two-factor authenticator app sync failure', 'Changed smartphones and lost authenticator codes. Need identity verification to reset 2FA keys.', 'HIGH', 'IN_PROGRESS', NULL, NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 10 HOUR), 0, DATE_SUB(NOW(), INTERVAL 16 HOUR), NOW(), NULL, NULL),

(19, 'TCK-2026-019', 3, 3, 3, 'Bank transfer reference required for manual reconciliation', 'We received notification of your bank deposit for ORD-98221. Please attach bank slip copy with transaction reference.', 'MEDIUM', 'WAITING_FOR_CUSTOMER', 'ORD-98221', NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 48 HOUR), 0, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NULL, NULL),
(20, 'TCK-2026-020', 6, 1, 2, 'Confirmation of alternate delivery address needed', 'Courier reported gated community access restricted. Please confirm security contact or alternate neighbor address.', 'LOW', 'WAITING_FOR_CUSTOMER', 'ORD-98229', 'TRK-LK-44120', 'NONE', DATE_ADD(NOW(), INTERVAL 36 HOUR), 0, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), NULL, NULL),
(21, 'TCK-2026-021', 7, 5, 4, 'Serial number required for extended warranty registration', 'To issue your 2-year warranty certificate, please reply with the 12-digit barcode serial number on the rear panel.', 'LOW', 'WAITING_FOR_CUSTOMER', 'ORD-98231', NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 40 HOUR), 0, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NULL, NULL),
(22, 'TCK-2026-022', 8, 2, 2, 'Photographic proof requested for water damaged box', 'Please provide photos of the water damaged external carton so we can file an immediate claim with the courier partner.', 'MEDIUM', 'WAITING_FOR_CUSTOMER', 'ORD-98234', 'TRK-LK-33219', 'NONE', DATE_ADD(NOW(), INTERVAL 24 HOUR), 0, DATE_SUB(NOW(), INTERVAL 2 DAY), NOW(), NULL, NULL),
(23, 'TCK-2026-023', 17, 5, 4, 'Proof of purchase needed for replacement cable', 'Please upload tax invoice or order receipt showing purchase within standard 12-month period.', 'LOW', 'WAITING_FOR_CUSTOMER', 'ORD-98247', NULL, 'NONE', DATE_ADD(NOW(), INTERVAL 44 HOUR), 0, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NULL, NULL),

(24, 'TCK-2026-024', 5, 6, 1, 'SLA Breach: Overdue refund on defective industrial printer', 'Refund request submitted 14 days ago for defective unit under ORD-98226 has exceeded promised 5-day bank SLA.', 'URGENT', 'IN_PROGRESS', 'ORD-98226', 'TRK-LK-22105', 'PROCESSING', DATE_SUB(NOW(), INTERVAL 12 HOUR), 1, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(), NULL, NULL),
(25, 'TCK-2026-025', 2, 6, 5, 'Customer dissatisfaction with support officer response time', 'Customer expressing serious frustration over repeated delays in courier re-attempt scheduling.', 'HIGH', 'IN_PROGRESS', 'ORD-98218', 'TRK-LK-11984', 'NONE', DATE_SUB(NOW(), INTERVAL 4 HOUR), 1, DATE_SUB(NOW(), INTERVAL 4 DAY), NOW(), NULL, NULL),

(26, 'TCK-2026-026', 1, 3, 3, 'Invoice copy request for corporate tax filing', 'Provided official VAT tax invoice and commercial receipt for order ORD-98214.', 'LOW', 'RESOLVED', 'ORD-98214', NULL, 'NONE', DATE_SUB(NOW(), INTERVAL 10 DAY), 0, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NULL),
(27, 'TCK-2026-027', 2, 2, 2, 'Address correction prior to courier dispatch', 'Delivery address successfully updated from Colombo 03 to Bambalapitiya branch. Package delivered.', 'MEDIUM', 'RESOLVED', 'ORD-98217', 'TRK-LK-99812', 'NONE', DATE_SUB(NOW(), INTERVAL 14 DAY), 0, DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY), NULL),
(28, 'TCK-2026-028', 4, 4, 1, 'Password reset link expired', 'Generated verified security reset token and customer successfully authenticated.', 'MEDIUM', 'RESOLVED', NULL, NULL, 'NONE', DATE_SUB(NOW(), INTERVAL 7 DAY), 0, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), NULL),
(29, 'TCK-2026-029', 6, 3, 3, 'Approved full refund for damaged headset shipment', 'QA verified warehouse defect. Refund of $125.00 approved and processed to issuing bank.', 'HIGH', 'RESOLVED', 'ORD-98230', 'TRK-LK-88741', 'APPROVED', DATE_SUB(NOW(), INTERVAL 4 DAY), 0, DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY), NULL),
(30, 'TCK-2026-030', 7, 5, 4, 'Product replacement under 1-year standard warranty', 'Replacement adapter dispatched under replacement waybill TRK-LK-77610.', 'MEDIUM', 'RESOLVED', 'ORD-98231', 'TRK-LK-77610', 'NONE', DATE_SUB(NOW(), INTERVAL 5 DAY), 0, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NULL),
(31, 'TCK-2026-031', 9, 1, 2, 'Wrong item variant received in parcel', 'Correct item dispatched via express courier and returned item collected free of charge.', 'HIGH', 'RESOLVED', 'ORD-98235', 'TRK-LK-66512', 'NONE', DATE_SUB(NOW(), INTERVAL 3 DAY), 0, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NULL),
(32, 'TCK-2026-032', 10, 2, 6, 'Courier tracking update request for delayed electronics shipment', 'Escalated with central logistics terminal. Package cleared and delivered on Thursday.', 'MEDIUM', 'RESOLVED', 'ORD-98239', 'TRK-LK-55120', 'NONE', DATE_SUB(NOW(), INTERVAL 2 DAY), 0, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NULL),
(33, 'TCK-2026-033', 11, 4, 5, 'Profile name and business tax identification update', 'Customer business documentation validated and profile updated in compliance with KYC guidelines.', 'LOW', 'RESOLVED', NULL, NULL, 'NONE', DATE_SUB(NOW(), INTERVAL 6 DAY), 0, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY), NULL),
(34, 'TCK-2026-034', 18, 3, 3, 'Billing query on shipping surcharge charge', 'Explained remote delivery area surcharge based on courier tariff guidelines. Customer satisfied.', 'LOW', 'RESOLVED', 'ORD-98248', NULL, 'NONE', DATE_SUB(NOW(), INTERVAL 3 DAY), 0, DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NULL),
(35, 'TCK-2026-035', 19, 5, 4, 'Setup assistance for smart temperature sensor array', 'Provided calibration guide and MQTT connectivity settings for IoT temperature sensor.', 'MEDIUM', 'RESOLVED', NULL, NULL, 'NONE', DATE_SUB(NOW(), INTERVAL 2 DAY), 0, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), NULL),
(36, 'TCK-2026-036', 20, 1, 1, 'Inquiry on cancellation refund status', 'Confirmed $95.00 reversal initiated to customer credit card with bank reference ARN-882109.', 'HIGH', 'RESOLVED', NULL, NULL, 'APPROVED', DATE_SUB(NOW(), INTERVAL 1 DAY), 0, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),
(37, 'TCK-2026-037', 3, 2, 6, 'Delayed delivery inquiry for Kandy regional parcel', 'Driver completed delivery at 4:30 PM with recipient digital signature capture.', 'MEDIUM', 'RESOLVED', 'ORD-98236', 'TRK-LK-44091', 'NONE', DATE_SUB(NOW(), INTERVAL 1 DAY), 0, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY), NULL),

(38, 'TCK-2026-038', 1, 1, 1, 'Inquiry regarding seasonal promotional discount codes', 'Clarified terms of holiday discount voucher codes. Customer confirmed issue resolved.', 'LOW', 'CLOSED', NULL, NULL, 'NONE', DATE_SUB(NOW(), INTERVAL 20 DAY), 0, DATE_SUB(NOW(), INTERVAL 22 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 18 DAY)),
(39, 'TCK-2026-039', 2, 3, 3, 'Full refund completed for canceled order ORD-98220', 'Bank transaction credit confirmed by customer. Refund state updated to REFUNDED.', 'HIGH', 'CLOSED', 'ORD-98220', NULL, 'REFUNDED', DATE_SUB(NOW(), INTERVAL 15 DAY), 0, DATE_SUB(NOW(), INTERVAL 18 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 16 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY)),
(40, 'TCK-2026-040', 3, 2, 2, 'Delivery delay due to inclement weather advisory', 'Courier completed final delivery following clearance of regional weather road closures.', 'MEDIUM', 'CLOSED', 'ORD-98222', 'TRK-LK-55421', 'NONE', DATE_SUB(NOW(), INTERVAL 12 DAY), 0, DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 13 DAY), DATE_SUB(NOW(), INTERVAL 11 DAY)),
(41, 'TCK-2026-041', 4, 5, 4, 'Setup and configuration guide for network switch', 'Provided step-by-step PDF manual and CLI terminal commands for VLAN tagging setup.', 'LOW', 'CLOSED', NULL, NULL, 'NONE', DATE_SUB(NOW(), INTERVAL 10 DAY), 0, DATE_SUB(NOW(), INTERVAL 12 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),
(42, 'TCK-2026-042', 8, 4, 5, 'Account unlock following multiple failed login attempts', 'Identity confirmed via phone verification. Security lockout cleared and password reset.', 'HIGH', 'CLOSED', NULL, NULL, 'NONE', DATE_SUB(NOW(), INTERVAL 8 DAY), 0, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY)),
(43, 'TCK-2026-043', 9, 3, 3, 'Duplicate subscription charge reversal confirmation', 'Refund batch FT-90124 processed to account. Funds reflected in customer card statement.', 'HIGH', 'CLOSED', 'ORD-98237', NULL, 'REFUNDED', DATE_SUB(NOW(), INTERVAL 7 DAY), 0, DATE_SUB(NOW(), INTERVAL 9 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 6 DAY)),
(44, 'TCK-2026-044', 12, 1, 1, 'Clarification on return window policy for open box items', 'Confirmed standard 7-day return policy applies to open box electronics. Case concluded.', 'LOW', 'CLOSED', 'ORD-98242', NULL, 'NONE', DATE_SUB(NOW(), INTERVAL 9 DAY), 0, DATE_SUB(NOW(), INTERVAL 11 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY)),
(45, 'TCK-2026-045', 14, 2, 6, 'Order delivery completed and signed by authorized recipient', 'Delivery confirmation received from logistics terminal. Case marked closed.', 'MEDIUM', 'CLOSED', 'ORD-98244', 'TRK-LK-22019', 'NONE', DATE_SUB(NOW(), INTERVAL 6 DAY), 0, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY));

-- 8. TICKET_REPLIES
INSERT INTO ticket_replies (id, ticket_id, user_id, message, is_internal, created_at) VALUES
(1, 1, 1, 'Hello, I opened the shipment box for ORD-98214 and noticed the wireless mouse was absent. Please advise.', 0, DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(2, 1, 2, 'Dear John, thank you for reaching out. We are verifying the packing video from our fulfillment center.', 0, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(3, 1, 3, 'Internal Note: Warehouse supervisor has confirmed a packing omission at Station 4.', 1, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(4, 2, 6, 'I noticed two separate debits of $110 on my credit card statement for a single order attempt.', 0, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(5, 2, 2, 'Hello Sarah, we apologize for the inconvenience. Our finance team is reviewing the payment gateway logs.', 0, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(6, 5, 11, 'The tracking milestone for ORD-98232 has been stationary for 4 days. Can you investigate?', 0, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(7, 5, 25, 'Hello Malik, I have pinged the Central Sorting Hub manager. An updated scan will be logged today.', 0, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(8, 9, 1, 'Could you please confirm if the replacement unit will arrive before Thursday evening?', 0, DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(9, 9, 2, 'Hi John, I have flagged your shipment as Priority Express with our dispatch team.', 0, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(10, 9, 1, 'Thank you very much for the swift follow-up!', 0, DATE_SUB(NOW(), INTERVAL 18 HOUR)),
(11, 10, 8, 'Can you confirm when the refund of $180 will reflect in my Commercial Bank account?', 0, DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(12, 10, 26, 'Hello Dilani, once QA inspection is marked completed, the banking batch takes 2 to 3 business days.', 0, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(13, 11, 9, 'The courier delivered the carton box completely crushed on one corner. The glass panel is fractured.', 0, DATE_SUB(NOW(), INTERVAL 46 HOUR)),
(14, 11, 25, 'Dear Rohan, we sincerely apologize. Please do not discard the packaging as our courier will inspect it.', 0, DATE_SUB(NOW(), INTERVAL 40 HOUR)),
(15, 11, 9, 'I have preserved all original wrapping and protective foam.', 0, DATE_SUB(NOW(), INTERVAL 36 HOUR)),
(16, 11, 25, 'Thank you. We have arranged an exchange collection for tomorrow morning.', 0, DATE_SUB(NOW(), INTERVAL 30 HOUR)),
(17, 12, 12, 'Please update our company billing email to finance@urbanmart.lk.', 0, DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(18, 12, 28, 'We are verifying company authorization letter. Changes will take effect shortly.', 0, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(19, 13, 13, 'Could you provide the direct link and checksum for the latest v3.4.2 gateway firmware?', 0, DATE_SUB(NOW(), INTERVAL 18 HOUR)),
(20, 13, 27, 'Hello Chamari, the patch file and SHA-256 verification hash have been sent to your registered email.', 0, DATE_SUB(NOW(), INTERVAL 14 HOUR)),
(21, 14, 6, 'Please cancel order ORD-98219 before the fulfillment center packs it.', 0, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(22, 14, 2, 'Canceled request routed to fulfillment bay. Cancellation confirmation pending.', 0, DATE_SUB(NOW(), INTERVAL 10 HOUR)),
(23, 15, 17, 'The courier left the package at Block B instead of Block D where I reside.', 0, DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(24, 15, 29, 'We have contacted courier driver Sunil to retrieve and hand over the parcel to your apartment block.', 0, DATE_SUB(NOW(), INTERVAL 18 HOUR)),
(25, 16, 18, 'Promo code SPRING20 gave only 5% discount instead of advertised 20%.', 0, DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(26, 16, 26, 'We have identified a coupon rule mismatch and our finance officer is crediting the $17 difference.', 0, DATE_SUB(NOW(), INTERVAL 16 HOUR)),
(27, 17, 19, 'Please switch size from Medium to XL on order ORD-98245.', 0, DATE_SUB(NOW(), INTERVAL 14 HOUR)),
(28, 17, 25, 'Updated pick list sent to warehouse for XL size variant.', 0, DATE_SUB(NOW(), INTERVAL 10 HOUR)),
(29, 18, 20, 'Lost 2FA authenticator app access due to mobile device upgrade.', 0, DATE_SUB(NOW(), INTERVAL 16 HOUR)),
(30, 18, 28, 'Identity verification initiated. Please answer the security questionnaire sent via SMS.', 0, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(31, 19, 26, 'Dear Kasun, to complete your manual order verification, please upload your bank transfer deposit slip.', 0, DATE_SUB(NOW(), INTERVAL 70 HOUR)),
(32, 19, 7, 'Understood, I am downloading the PDF receipt from my online banking app now.', 0, DATE_SUB(NOW(), INTERVAL 60 HOUR)),
(33, 20, 25, 'Dear Sarah, please confirm alternate security contact number for parcel delivery.', 0, DATE_SUB(NOW(), INTERVAL 46 HOUR)),
(34, 20, 6, 'You can reach building supervisor Mr. Perera at +94 77 1122334.', 0, DATE_SUB(NOW(), INTERVAL 36 HOUR)),
(35, 21, 27, 'Dear Malik, please share the 12-digit barcode serial number on the rear panel of your device.', 0, DATE_SUB(NOW(), INTERVAL 70 HOUR)),
(36, 21, 11, 'Barcode number is SN-98214-LK-0091.', 0, DATE_SUB(NOW(), INTERVAL 50 HOUR)),
(37, 22, 25, 'Dear Dilani, please upload photos of the damp outer carton for courier claim filing.', 0, DATE_SUB(NOW(), INTERVAL 46 HOUR)),
(38, 22, 8, 'Attached 2 photos showing the carton moisture damage.', 0, DATE_SUB(NOW(), INTERVAL 30 HOUR)),
(39, 23, 27, 'Dear Kanishka, please provide copy of original tax invoice or retail receipt.', 0, DATE_SUB(NOW(), INTERVAL 68 HOUR)),
(40, 23, 21, 'Attached receipt scan from initial purchase.', 0, DATE_SUB(NOW(), INTERVAL 40 HOUR)),
(41, 24, 9, 'It has been 14 days and I have still not received my refund. This is completely unacceptable.', 0, DATE_SUB(NOW(), INTERVAL 118 HOUR)),
(42, 24, 2, 'We are escalating this directly to Operations Supervisor and Head of Finance immediately.', 0, DATE_SUB(NOW(), INTERVAL 100 HOUR)),
(43, 24, 3, 'Internal Note: Escalated to Finance Director. Wire transfer authorization reference WT-90214 requested.', 1, DATE_SUB(NOW(), INTERVAL 72 HOUR)),
(44, 24, 4, 'Internal Note: Priority expedited approval granted by Customer Support Manager.', 1, DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(45, 24, 2, 'Dear Rohan, your refund has been processed under emergency wire batch. Funds will reflect within 24 hours.', 0, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(46, 25, 6, 'Your courier promised delivery 3 times and never showed up. No one is taking accountability.', 0, DATE_SUB(NOW(), INTERVAL 90 HOUR)),
(47, 25, 28, 'Dear Sarah, we deeply regret this service breakdown. I have taken personal ownership of your case.', 0, DATE_SUB(NOW(), INTERVAL 80 HOUR)),
(48, 25, 3, 'Internal Note: QA Executive notified to review courier performance logs for Bambalapitiya route.', 1, DATE_SUB(NOW(), INTERVAL 50 HOUR)),
(49, 26, 1, 'Please provide the official corporate VAT invoice for our accounting audit.', 0, DATE_SUB(NOW(), INTERVAL 12 DAY)),
(50, 26, 26, 'Hello John, your tax invoice INV-2026-98214 has been generated and attached to this ticket.', 0, DATE_SUB(NOW(), INTERVAL 11 DAY)),
(51, 26, 1, 'Received and verified. Thank you!', 0, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(52, 26, 26, 'You are very welcome. Marking ticket as resolved.', 0, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(53, 27, 6, 'Please redirect order ORD-98217 to our Colombo 04 branch office.', 0, DATE_SUB(NOW(), INTERVAL 16 DAY)),
(54, 27, 25, 'Address updated in courier dispatch system. New waybill generated.', 0, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(55, 27, 6, 'Package received safely at the new address. Great job!', 0, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(56, 28, 8, 'My password reset link shows expired when I click it from my email.', 0, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(57, 28, 2, 'We have generated a fresh high-priority secure reset token sent directly to your email.', 0, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(58, 28, 8, 'Successfully logged in now. Thanks!', 0, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(59, 29, 10, 'Requesting refund approval for defective headset under warranty.', 0, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(60, 29, 26, 'Defect report approved by technical assessment. Refund credited to card.', 0, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(61, 30, 11, 'Power adapter ceased functioning after 3 weeks of usage.', 0, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(62, 30, 27, 'Replacement adapter unit dispatched via courier with tracking TRK-LK-77610.', 0, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(63, 31, 13, 'I ordered the 16GB RAM model but received the 8GB version in the parcel.', 0, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(64, 31, 25, 'Correct 16GB unit dispatched via express courier with return pickup bag.', 0, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(65, 32, 14, 'Urgent inquiry regarding delayed medical equipment package.', 0, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(66, 32, 29, 'Fast-tracked through customs logistics checkpoint and delivered.', 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(67, 33, 15, 'Requesting company tax registration number update.', 0, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(68, 33, 28, 'Tax identification number registered and verified.', 0, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(69, 34, 22, 'Why was a remote delivery surcharge applied to our shipment?', 0, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(70, 34, 26, 'Explained postal zone classifications. Customer confirmed understanding.', 0, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(71, 35, 23, 'Need MQTT connection endpoint details for sensor array.', 0, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(72, 35, 27, 'Provided TLS connection string and server certificates.', 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(73, 36, 24, 'When will the refunded amount reflect on my credit card?', 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(74, 36, 2, 'Bank reversal reference ARN-882109 provided. Credited to card.', 0, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(75, 37, 7, 'Delivery status update for Kandy destination parcel.', 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(76, 37, 29, 'Driver completed delivery with customer signature verification.', 0, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(77, 38, 1, 'Are student promotional discounts applicable on enterprise software packages?', 0, DATE_SUB(NOW(), INTERVAL 22 DAY)),
(78, 38, 2, 'Student discounts apply to individual licenses. Enterprise tiers have volume pricing.', 0, DATE_SUB(NOW(), INTERVAL 20 DAY)),
(79, 39, 6, 'Please confirm refund status for canceled order ORD-98220.', 0, DATE_SUB(NOW(), INTERVAL 18 DAY)),
(80, 39, 26, 'Refund transaction FT-88910 completed. Amount $110.00 returned to card.', 0, DATE_SUB(NOW(), INTERVAL 16 DAY)),
(81, 40, 7, 'Has delivery resumed for Kandy hill country routes?', 0, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(82, 40, 25, 'Yes, road clearances are complete and parcel is out for delivery today.', 0, DATE_SUB(NOW(), INTERVAL 13 DAY)),
(83, 41, 8, 'Could you share the VLAN configuration guide for our managed switch?', 0, DATE_SUB(NOW(), INTERVAL 12 DAY)),
(84, 41, 27, 'Detailed PDF guide and CLI reference attached for your network engineer.', 0, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(85, 42, 12, 'Account locked out after entering incorrect password multiple times.', 0, DATE_SUB(NOW(), INTERVAL 10 DAY));

-- 9. TICKET_HISTORY
INSERT INTO ticket_history (id, ticket_id, performed_by_id, action, old_value, new_value, timestamp) VALUES
(1, 1, 1, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(2, 2, 6, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(3, 3, 7, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
(4, 4, 10, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(5, 5, 11, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(6, 6, 14, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 7 HOUR)),
(7, 7, 15, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(8, 8, 16, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 4 HOUR)),

(9, 9, 1, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(10, 9, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Demo Support Officer (AGT-1001)', DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(11, 9, 2, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 20 HOUR)),

(12, 10, 8, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(13, 10, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Kavindi Jayawardena (AGT-1003)', DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(14, 10, 26, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(15, 10, 26, 'REFUND_STATUS_CHANGED', 'REQUESTED', 'PROCESSING', DATE_SUB(NOW(), INTERVAL 18 HOUR)),

(16, 11, 9, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 48 HOUR)),
(17, 11, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Nuwan Bandara (AGT-1002)', DATE_SUB(NOW(), INTERVAL 44 HOUR)),
(18, 11, 25, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 40 HOUR)),

(19, 12, 12, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(20, 12, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Nimesha Rathnayake (AGT-1005)', DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(21, 12, 28, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 20 HOUR)),

(22, 13, 13, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 18 HOUR)),
(23, 13, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Sajith Wickramasinghe (AGT-1004)', DATE_SUB(NOW(), INTERVAL 16 HOUR)),
(24, 13, 27, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 14 HOUR)),

(25, 14, 6, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(26, 14, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Demo Support Officer (AGT-1001)', DATE_SUB(NOW(), INTERVAL 11 HOUR)),
(27, 14, 2, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 10 HOUR)),

(28, 15, 17, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(29, 15, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Dharshana Abeykoon (AGT-1006)', DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(30, 15, 29, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 18 HOUR)),

(31, 16, 18, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(32, 16, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Kavindi Jayawardena (AGT-1003)', DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(33, 16, 26, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 16 HOUR)),

(34, 17, 19, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 14 HOUR)),
(35, 17, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Nuwan Bandara (AGT-1002)', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(36, 17, 25, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 10 HOUR)),

(37, 18, 20, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 16 HOUR)),
(38, 18, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Nimesha Rathnayake (AGT-1005)', DATE_SUB(NOW(), INTERVAL 14 HOUR)),
(39, 18, 28, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 12 HOUR)),

(40, 19, 7, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 72 HOUR)),
(41, 19, 26, 'STATUS_CHANGED', 'OPEN', 'WAITING_FOR_CUSTOMER', DATE_SUB(NOW(), INTERVAL 70 HOUR)),

(42, 20, 10, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 48 HOUR)),
(43, 20, 25, 'STATUS_CHANGED', 'OPEN', 'WAITING_FOR_CUSTOMER', DATE_SUB(NOW(), INTERVAL 46 HOUR)),

(44, 21, 11, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 72 HOUR)),
(45, 21, 27, 'STATUS_CHANGED', 'OPEN', 'WAITING_FOR_CUSTOMER', DATE_SUB(NOW(), INTERVAL 70 HOUR)),

(46, 22, 12, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 48 HOUR)),
(47, 22, 25, 'STATUS_CHANGED', 'OPEN', 'WAITING_FOR_CUSTOMER', DATE_SUB(NOW(), INTERVAL 46 HOUR)),

(48, 23, 21, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 72 HOUR)),
(49, 23, 27, 'STATUS_CHANGED', 'OPEN', 'WAITING_FOR_CUSTOMER', DATE_SUB(NOW(), INTERVAL 68 HOUR)),

(50, 24, 9, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 120 HOUR)),
(51, 24, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Demo Support Officer (AGT-1001)', DATE_SUB(NOW(), INTERVAL 115 HOUR)),
(52, 24, 2, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 110 HOUR)),
(53, 24, 3, 'ESCALATED', 'IN_PROGRESS', 'ESCALATED', DATE_SUB(NOW(), INTERVAL 72 HOUR)),
(54, 24, 3, 'PRIORITY_CHANGED', 'HIGH', 'URGENT', DATE_SUB(NOW(), INTERVAL 72 HOUR)),

(55, 25, 6, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 96 HOUR)),
(56, 25, 3, 'AGENT_ASSIGNED', 'Unassigned', 'Nimesha Rathnayake (AGT-1005)', DATE_SUB(NOW(), INTERVAL 90 HOUR)),
(57, 25, 28, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 80 HOUR)),
(58, 25, 3, 'ESCALATED', 'IN_PROGRESS', 'ESCALATED', DATE_SUB(NOW(), INTERVAL 48 HOUR)),

(59, 26, 1, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 12 DAY)),
(60, 26, 26, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 11 DAY)),
(61, 26, 26, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 10 DAY)),

(62, 27, 6, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 16 DAY)),
(63, 27, 25, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 15 DAY)),
(64, 27, 25, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 15 DAY)),

(65, 28, 8, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 8 DAY)),
(66, 28, 2, 'STATUS_CHANGED', 'OPEN', 'IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(67, 28, 2, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 7 DAY)),

(68, 29, 10, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(69, 29, 26, 'REFUND_STATUS_CHANGED', 'REQUESTED', 'APPROVED', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(70, 29, 26, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 4 DAY)),

(71, 30, 11, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(72, 30, 27, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 5 DAY)),

(73, 31, 13, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(74, 31, 25, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 3 DAY)),

(75, 32, 14, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(76, 32, 29, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 2 DAY)),

(77, 33, 15, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 8 DAY)),
(78, 33, 28, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 6 DAY)),

(79, 34, 22, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(80, 34, 26, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 3 DAY)),

(81, 35, 23, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(82, 35, 27, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 2 DAY)),

(83, 36, 24, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(84, 36, 2, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 1 DAY)),

(85, 37, 7, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(86, 37, 29, 'STATUS_CHANGED', 'IN_PROGRESS', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 1 DAY)),

(87, 38, 1, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 22 DAY)),
(88, 38, 2, 'STATUS_CHANGED', 'OPEN', 'RESOLVED', DATE_SUB(NOW(), INTERVAL 20 DAY)),
(89, 38, 2, 'STATUS_CHANGED', 'RESOLVED', 'CLOSED', DATE_SUB(NOW(), INTERVAL 18 DAY)),

(90, 39, 6, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: HIGH', DATE_SUB(NOW(), INTERVAL 18 DAY)),
(91, 39, 26, 'REFUND_STATUS_CHANGED', 'APPROVED', 'REFUNDED', DATE_SUB(NOW(), INTERVAL 16 DAY)),
(92, 39, 26, 'STATUS_CHANGED', 'RESOLVED', 'CLOSED', DATE_SUB(NOW(), INTERVAL 14 DAY)),

(93, 40, 7, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: MEDIUM', DATE_SUB(NOW(), INTERVAL 15 DAY)),
(94, 40, 25, 'STATUS_CHANGED', 'RESOLVED', 'CLOSED', DATE_SUB(NOW(), INTERVAL 11 DAY)),

(95, 41, 8, 'TICKET_CREATED', NULL, 'Status: OPEN, Priority: LOW', DATE_SUB(NOW(), INTERVAL 12 DAY));

-- 10. ATTACHMENTS
INSERT INTO attachments (id, ticket_id, reply_id, original_file_name, stored_file_name, file_type, file_size, file_path, uploaded_at) VALUES
(1, 1, 1, 'unboxing_missing_item.jpg', 'att_98214_unboxing_01.jpg', 'image/jpeg', 245820, 'uploads/att_98214_unboxing_01.jpg', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(2, 2, 4, 'bank_double_charge_statement.pdf', 'att_bank_statement_98220.pdf', 'application/pdf', 512400, 'uploads/att_bank_statement_98220.pdf', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(3, 11, 13, 'crushed_carton_exterior.png', 'att_crushed_carton_exterior.png', 'image/png', 840120, 'uploads/att_crushed_carton_exterior.png', DATE_SUB(NOW(), INTERVAL 46 HOUR)),
(4, 11, 13, 'cracked_internal_glass.jpg', 'att_cracked_internal_glass.jpg', 'image/jpeg', 620450, 'uploads/att_cracked_internal_glass.jpg', DATE_SUB(NOW(), INTERVAL 46 HOUR)),
(5, 19, 32, 'bank_deposit_slip_proof.pdf', 'att_bank_deposit_slip_proof.pdf', 'application/pdf', 380100, 'uploads/att_bank_deposit_slip_proof.pdf', DATE_SUB(NOW(), INTERVAL 60 HOUR)),
(6, 22, 38, 'moisture_carton_evidence.jpg', 'att_moisture_carton_evidence.jpg', 'image/jpeg', 475200, 'uploads/att_moisture_carton_evidence.jpg', DATE_SUB(NOW(), INTERVAL 30 HOUR)),
(7, 23, 40, 'original_tax_receipt.pdf', 'att_original_tax_receipt.pdf', 'application/pdf', 298000, 'uploads/att_original_tax_receipt.pdf', DATE_SUB(NOW(), INTERVAL 40 HOUR)),
(8, 24, 41, 'sla_breach_timeline_summary.pdf', 'att_sla_breach_timeline.pdf', 'application/pdf', 410800, 'uploads/att_sla_breach_timeline.pdf', DATE_SUB(NOW(), INTERVAL 118 HOUR)),
(9, 26, 50, 'commercial_tax_invoice_98214.pdf', 'att_tax_invoice_98214.pdf', 'application/pdf', 215600, 'uploads/att_tax_invoice_98214.pdf', DATE_SUB(NOW(), INTERVAL 11 DAY)),
(10, 29, 59, 'headset_serial_diagnostic.png', 'att_headset_diagnostic.png', 'image/png', 490200, 'uploads/att_headset_diagnostic.png', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(11, 30, 61, 'power_adapter_warranty_card.jpg', 'att_power_adapter_warranty.jpg', 'image/jpeg', 312000, 'uploads/att_power_adapter_warranty.jpg', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(12, 31, 63, 'received_item_box_barcode.jpg', 'att_wrong_ram_barcode.jpg', 'image/jpeg', 425100, 'uploads/att_wrong_ram_barcode.jpg', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(13, 41, 84, 'vlan_managed_switch_manual.pdf', 'att_vlan_managed_switch_guide.pdf', 'application/pdf', 1250800, 'uploads/att_vlan_managed_switch_guide.pdf', DATE_SUB(NOW(), INTERVAL 10 DAY)),
(14, 42, 85, 'corporate_identity_letter.pdf', 'att_corporate_identity_auth.pdf', 'application/pdf', 345000, 'uploads/att_corporate_identity_auth.pdf', DATE_SUB(NOW(), INTERVAL 10 DAY));

-- 11. FEEDBACK
INSERT INTO feedback (id, ticket_id, customer_id, rating, comment, suggestions, created_at) VALUES
(1, 26, 1, 5, 'Outstanding service! The tax invoice was generated and sent within hours of my request.', 'Keep up the fantastic turnaround time.', DATE_SUB(NOW(), INTERVAL 9 DAY)),
(2, 27, 2, 5, 'The support officer proactively updated my address and the courier delivered without a single glitch.', 'Excellent proactive communication.', DATE_SUB(NOW(), INTERVAL 14 DAY)),
(3, 28, 4, 5, 'Prompt password recovery support. I was able to resume work on my portal immediately.', 'Self-service 2FA management would be a great future addition.', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(4, 29, 6, 5, 'Very impressed by the hassle-free refund policy for defective goods.', 'Transparent refund notifications via SMS would make it even better.', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(5, 33, 11, 5, 'Company tax details updated swiftly with proper verification.', 'Smooth administrative process.', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(6, 35, 19, 5, 'Technical support gave exact CLI commands and MQTT host specs. Solved our device integration.', 'Superb technical competency.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(7, 36, 20, 5, 'Immediate reversal authorization provided for my canceled order.', 'Great customer centric attitude.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(8, 37, 3, 5, 'Kandy parcel delivery completed smoothly despite bad weather.', 'Courier contact info in ticket is very useful.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(9, 38, 1, 5, 'Clear and detailed explanation of promotional voucher terms. Thank you!', 'None, everything was clear.', DATE_SUB(NOW(), INTERVAL 17 DAY)),
(10, 41, 4, 5, 'The technical guide and CLI command snippets provided by the agent solved our network issue immediately.', 'Kudos to the technical support team.', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(11, 44, 12, 5, 'Return window policy was explained politely and clearly.', 'Clear FAQ sections help a lot.', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(12, 45, 14, 5, 'Delivery was completed right on schedule and signed safely.', 'Excellent courier partner choice.', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(13, 30, 7, 4, 'Replacement adapter was delivered as promised. Good customer care.', 'Packaging could have been slightly more compact.', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(14, 31, 9, 4, 'Exchange was handled smoothly once the barcode was verified.', 'Courier collection arrived an hour later than scheduled.', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(15, 32, 10, 4, 'Tracking issue resolved after support officer intervened with courier.', 'Faster milestone tracking updates on web.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(16, 34, 18, 4, 'Surcharge breakdown was explained clearly by billing agent.', 'Make remote delivery zone surcharges visible at checkout.', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(17, 39, 2, 4, 'Refund reached my bank account safely. Overall positive resolution.', 'Faster automated email confirmation when refund is initiated.', DATE_SUB(NOW(), INTERVAL 13 DAY)),
(18, 40, 3, 4, 'Appreciate the agent keeping me informed during the regional weather delay.', 'Live GPS tracking on the map would be awesome.', DATE_SUB(NOW(), INTERVAL 10 DAY)),
(19, 42, 8, 4, 'Security verification was thorough and account was unlocked safely.', 'Support queue took around 10 minutes.', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(20, 43, 9, 4, 'Subscription charge reversal was processed cleanly.', 'Notification when bank initiates wire transfer.', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(21, 21, 7, 3, 'Agent was helpful but having to find the 12-digit serial number was cumbersome.', 'Barcode scanner in mobile web view would help.', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(22, 22, 8, 3, 'Resolution is progressing, but took some time to get initial response.', 'Faster initial acknowledgment needed.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(23, 23, 17, 3, 'Took multiple followups before warranty replacement slip was issued.', 'Streamline the proof of purchase submission.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(24, 25, 2, 2, 'Courier missed 3 delivery windows and initial support responses felt like automated canned scripts.', 'Hold third-party courier dispatchers strictly accountable.', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(25, 11, 5, 2, 'Still waiting on replacement dispatch confirmation after receiving broken goods.', 'Speed up damaged parcel re-dispatch approvals.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(26, 15, 13, 2, 'Package left in wrong block and required multiple calls to resolve.', 'Driver should confirm building number before dropoff.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(27, 24, 5, 1, 'Unacceptable 14-day delay for an industrial equipment refund. Required executive escalation to get basic action.', 'Re-train finance officers on strict SLA compliance adherence.', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(28, 16, 14, 1, 'Coupon discount failed silently at checkout and took 3 tickets to get a response.', 'Fix promotional discount calculation engine.', DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 12. FAQ_ARTICLES
INSERT INTO faq_articles (id, question, answer, category_id, is_published, keywords, view_count, created_by_id, created_at, updated_at) VALUES
(1, 'How can I track my online order and delivery status in real-time?', 'You can track your parcel live using your Tracking Number or Order ID in your customer dashboard or courier tracking portal. Standard island-wide shipping takes 2 to 4 business days.', 1, 1, 'track,tracking,order,delivery,status,where is my order,courier,shipment,waybill', 342, 4, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(2, 'What is the return policy for damaged or defective items?', 'You can request a return within 7 calendar days of delivery receipt. Please submit a support ticket attaching photos of the damaged product and package barcode. Our logistics team arranges a free courier pickup.', 2, 1, 'return,refund,damaged,broken,wrong item,policy,exchange,defective', 415, 4, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(3, 'How long does an approved refund take to process to my bank or card?', 'Once our Quality Assurance team approves the returned item, card refunds are credited within 3 to 5 business days depending on your issuing bank. Bank transfers are typically completed within 48 hours.', 3, 1, 'refund,money back,card,bank,how long,turnaround,payment return,credit', 280, 4, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(4, 'What should I do if my payment was debited but the order failed?', 'If your bank account or card was charged but you did not receive an Order Confirmation, the funds are usually automatically reversed by your bank within 24 hours. If not, submit a ticket under Payment Disputes with your bank transaction reference.', 3, 1, 'payment,debited,charged,failed,transaction,gateway,card deducted,duplicate', 195, 4, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(5, 'How do I apply promotional discount vouchers or coupons?', 'Enter your promotional voucher code at the Checkout stage under the Voucher Code field before clicking Place Order. Each order allows one promotional code.', 5, 1, 'voucher,coupon,promo,discount,code,apply,offer,seasonal', 164, 4, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(6, 'How can I reset my account password if I forgot it?', 'Click Forgot Password on the login page and enter your registered email address. A secure one-time password reset link will be sent to your inbox within 2 minutes.', 4, 1, 'forgot password,reset password,login problem,account locked,recovery', 310, 4, DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),
(7, 'Can I change my delivery address after placing an order?', 'You can request a delivery address modification before your order status changes to SHIPPED by creating a support ticket or contacting support officer immediately.', 2, 1, 'change address,update address,wrong address,shipping location,redirect', 225, 4, DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),
(8, 'How do I cancel an order before it has been dispatched?', 'Navigate to My Orders in your Customer Portal. If the order is in PROCESSING state, click Cancel Order. For orders in SHIPPED state, submit a ticket for return authorization.', 1, 1, 'cancel order,cancellation,stop order,cancel purchase,refund order', 188, 4, DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(9, 'What payment methods are supported on SupportHUB platform?', 'We accept Visa, MasterCard, American Express, LankaQR, online bank direct debits, and Cash on Delivery (COD) for eligible regions.', 3, 1, 'payment methods,visa,mastercard,amex,credit card,debit card,cash on delivery,cod', 145, 4, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(10, 'What is covered under the standard manufacturer product warranty?', 'Manufacturer warranty covers internal component failures, hardware manufacturing defects, and factory faults. Physical breakage, liquid damage, and unauthorized modifications are excluded.', 5, 1, 'warranty,guarantee,repair,claim,manufacturing defect,coverage', 178, 4, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(11, 'How do I escalate an unresolved ticket to senior management?', 'If your ticket has exceeded standard resolution SLA without a satisfactory outcome, click the Escalate Ticket button on your ticket details page or select Complaints & Escalations category.', 6, 1, 'escalate,escalation,supervisor,manager,complaint,dissatisfaction,unresolved', 156, 4, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),
(12, 'How do I enable Two-Factor Authentication (2FA) for extra security?', 'Go to Profile Settings > Security > Two-Factor Authentication. Scan the QR code using Google Authenticator or Microsoft Authenticator and enter the 6-digit confirmation PIN.', 4, 1, '2fa,two factor,security,authenticator,qr code,pin,otp,protect account', 132, 4, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW());

-- 13. KNOWLEDGE_BASE_ARTICLES
INSERT INTO knowledge_base_articles (id, title, content, category_id, is_published, tags, view_count, created_by_id, created_at, updated_at) VALUES
(1, 'Complete Guide to Real-Time Order Tracking', '### Overview\nSupportHUB allows customers to monitor order milestones from fulfillment through courier delivery.\n\n### Step-by-Step Tracking:\n1. Log into your **Customer Dashboard**.\n2. Navigate to **My Orders**.\n3. Click on the active **Order Number** (e.g. `ORD-98214`).\n4. View checkpoint timestamps: `Order Placed` -> `Processing` -> `Dispatched` -> `Out for Delivery` -> `Completed`.\n\n### Courier Waybill Inquiries:\nIf tracking updates have stalled for over 48 hours, submit a support ticket under **Delivery Issues**.', 2, 1, 'tracking,order,delivery,courier,waybill', 1540, 3, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(2, 'Step-by-Step Product Return and Refund Procedure', '### Return Eligibility\n- Items must be reported within **7 calendar days** of delivery.\n- Original tags, packaging, and accessories must remain intact.\n\n### How to Initiate a Return:\n1. Open a new ticket under **Category: Order Issues > Returns & Exchanges**.\n2. Enter your Order Number and state the return reason.\n3. Attach clear photographs of the product and barcode label.\n4. A support officer will review and schedule a free return courier pickup.\n\n### Refund Turnaround:\n- **Card Refunds:** 3 to 5 business days.\n- **Bank Direct Transfers:** 24 to 48 hours.', 3, 1, 'refund,return,reimbursement,finance,dispute', 1280, 3, DATE_SUB(NOW(), INTERVAL 45 DAY), NOW()),
(3, 'Handling Damaged or Broken Shipments on Delivery', '### Immediate Steps Upon Receiving Damaged Goods:\n1. **Do not dispose** of any external packaging or foam wrapping.\n2. Take 3 clear photos: (a) External carton damage, (b) Courier waybill label, (c) Damaged product.\n3. Log a support ticket under **Delivery Issues > Damaged In-Transit Packages** within 48 hours.\n4. Our logistics team will dispatch an immediate priority replacement unit.', 1, 1, 'damaged,broken,transit,inspection,replacement', 960, 3, DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),
(4, 'Account Security and Password Recovery Guide', '### Securing Your SupportHUB Profile\n- Choose a strong password containing minimum 8 characters, uppercase, lowercase, numbers, and symbols.\n- Never share verification OTPs or reset links with anyone.\n\n### Resetting Forgotten Passwords:\n1. Click **Forgot Password** on the login screen.\n2. Check your registered email for the single-use recovery link.\n3. Enter your new password and confirm.', 4, 1, 'security,password,login,account,recovery', 1420, 3, DATE_SUB(NOW(), INTERVAL 40 DAY), NOW()),
(5, 'Accepted Payment Gateways and Billing Troubleshooting', '### Supported Payment Channels\n- Visa, MasterCard, AMEX (Processed via 3D-Secure 2.0)\n- LankaQR mobile payments\n- Direct corporate bank wire transfers\n\n### Resolving Payment Declines:\n- Ensure international e-commerce transactions are activated on your bank card.\n- If funds were deducted for a failed checkout, banks auto-reverse within 24 hours.', 3, 1, 'payment,billing,gateway,checkout,cards', 890, 3, DATE_SUB(NOW(), INTERVAL 35 DAY), NOW()),
(6, 'Understanding Ticket Priorities and SLA Commitments', '### SupportHUB Service Level Agreement (SLA) Matrix\n- **URGENT:** First response within 2 hours | Resolution within 12 hours (Critical system downtime, payment double charges)\n- **HIGH:** First response within 4 hours | Resolution within 24 hours (Damaged items, order cancellation)\n- **MEDIUM:** First response within 8 hours | Resolution within 48 hours (General tracking, inquiries)\n- **LOW:** First response within 24 hours | Resolution within 72 hours (General specs, feedback)', 6, 1, 'sla,priority,service level,escalation,resolution', 1150, 4, DATE_SUB(NOW(), INTERVAL 30 DAY), NOW()),
(7, 'How to Claim Manufacturer Warranty Support', '### Warranty Claim Checklist:\n1. Original Order Number or Tax Invoice.\n2. Device serial number from the product casing.\n3. Brief video or photo demonstrating the hardware malfunction.\n\nSubmit your claim under **Product Information > Warranty Information** for fast-track authorized service center clearance.', 5, 1, 'warranty,guarantee,hardware,service,repair', 720, 3, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW()),
(8, 'Configuring Two-Factor Authentication (2FA) Security', '### Why Enable 2FA?\nTwo-Factor Authentication adds an essential second verification barrier against unauthorized access.\n\n### Setup Steps:\n1. Go to **Settings > Security**.\n2. Select **Enable 2FA**.\n3. Scan the barcode with your mobile Authenticator application.\n4. Save your emergency recovery backup codes in a secure location.', 4, 1, '2fa,two factor,security,authenticator,authentication', 640, 4, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW()),
(9, 'Managing Corporate Bulk Orders and Invoicing', '### Commercial and Tax Invoices\nCorporate clients requiring formalized VAT invoices or credit facilities can request digital invoices directly from ticket details.\n\nOur accounts department automatically issues certified electronic PDF invoices for corporate tax deductions.', 3, 1, 'corporate,invoice,vat,tax,bulk orders,business', 530, 3, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW()),
(10, 'Customer Service Grievances and Escalation Procedures', '### Multi-Tier Escalation Hierarchy\n- **Tier 1 (Customer Service Officer):** Handles initial triage and standard resolutions.\n- **Tier 2 (Operations Supervisor):** Intervenes on SLA delays, agent reassignment, and exceptions.\n- **Tier 3 (Customer Support Manager & QA):** Final dispute resolution, executive refunds, and policy enforcement.', 6, 1, 'grievance,complaint,escalation,management,qa', 680, 4, DATE_SUB(NOW(), INTERVAL 10 DAY), NOW());

-- 14. NOTIFICATIONS
INSERT INTO notifications (id, user_id, title, message, type, related_ticket_id, is_read, created_at) VALUES
(1, 1, 'Ticket Created Successfully', 'Your ticket TCK-2026-001 regarding missing item has been logged.', 'TICKET_CREATED', 1, 0, DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(2, 1, 'Agent Assigned to Your Ticket', 'Demo Support Officer has been assigned to ticket TCK-2026-009.', 'TICKET_ASSIGNED', 9, 1, DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(3, 1, 'New Reply Received', 'Demo Support Officer replied to your ticket TCK-2026-009.', 'TICKET_REPLIED', 9, 1, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(4, 1, 'Ticket Resolved', 'Ticket TCK-2026-026 has been marked as RESOLVED.', 'TICKET_RESOLVED', 26, 1, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(5, 1, 'How was our service?', 'Please take a moment to provide feedback for resolved ticket TCK-2026-026.', 'FEEDBACK_REMINDER', 26, 1, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(6, 2, 'New Ticket Assigned', 'You have been assigned to handle ticket TCK-2026-009 (High Priority).', 'TICKET_ASSIGNED', 9, 0, DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(7, 2, 'New Customer Message', 'Customer John Doe replied on ticket TCK-2026-009.', 'TICKET_REPLIED', 9, 0, DATE_SUB(NOW(), INTERVAL 18 HOUR)),
(8, 2, 'New Ticket Assigned', 'You have been assigned to handle ticket TCK-2026-014.', 'TICKET_ASSIGNED', 14, 1, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(9, 2, 'Customer Feedback Received', 'Customer John Doe gave a 5-star rating on ticket TCK-2026-026.', 'FEEDBACK_REMINDER', 26, 1, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(10, 2, 'System Maintenance Notice', 'Scheduled platform database maintenance tonight from 2:00 AM to 3:00 AM UTC.', 'SYSTEM', NULL, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(11, 3, 'SLA Breach Warning', 'Ticket TCK-2026-024 has exceeded resolution SLA timeframe.', 'TICKET_STATUS_CHANGED', 24, 0, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(12, 3, 'Ticket Escalation Request', 'Customer Sarah Fernando escalated ticket TCK-2026-025 for supervisor review.', 'TICKET_STATUS_CHANGED', 25, 0, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(13, 3, 'Agent Workload Alert', 'Support Officer Nuwan Bandara reached 5 active in-progress tickets.', 'SYSTEM', NULL, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(14, 3, 'New Ticket Logged', 'High priority ticket TCK-2026-002 requires category assignment.', 'TICKET_CREATED', 2, 1, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(15, 3, 'Category Updated', 'Order Issues subcategories refreshed with new shipping rules.', 'SYSTEM', NULL, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(16, 4, 'Executive Escalation Summary', 'Ticket TCK-2026-024 pending executive management refund approval.', 'TICKET_STATUS_CHANGED', 24, 0, DATE_SUB(NOW(), INTERVAL 10 HOUR)),
(17, 4, 'Monthly CSAT Score Report', 'SupportHUB CSAT score for previous week reached 92.4% satisfaction.', 'SYSTEM', NULL, 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(18, 4, 'New Knowledge Base Article Published', 'Article: Complete Guide to Order Tracking is now live.', 'SYSTEM', NULL, 1, DATE_SUB(NOW(), INTERVAL 15 DAY)),
(19, 4, 'Staff Performance Review Ready', 'Bi-weekly officer KPI workload analytics compiled for executive review.', 'SYSTEM', NULL, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(20, 5, 'Low CSAT Review Triggered', '1-Star rating recorded on ticket TCK-2026-024. QA investigation required.', 'FEEDBACK_REMINDER', 24, 0, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(21, 5, 'Low CSAT Review Triggered', '2-Star rating recorded on ticket TCK-2026-025 for courier delay dispute.', 'FEEDBACK_REMINDER', 25, 0, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(22, 5, 'Low CSAT Review Triggered', '1-Star rating recorded on ticket TCK-2026-016 for coupon engine failure.', 'FEEDBACK_REMINDER', 16, 0, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(23, 5, 'QA Audit Complete', 'Weekly ticket reply audit report has been compiled and saved.', 'SYSTEM', NULL, 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(24, 5, 'CSAT Target Milestone Met', 'Positive feedback percentage exceeded 70% threshold.', 'SYSTEM', NULL, 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(25, 6, 'Ticket Assigned', 'Officer Kavindi Jayawardena is working on ticket TCK-2026-002.', 'TICKET_ASSIGNED', 2, 0, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(26, 6, 'Refund Approved', 'Full refund of $110.00 approved for order ORD-98220.', 'TICKET_STATUS_CHANGED', 39, 1, DATE_SUB(NOW(), INTERVAL 16 DAY)),
(27, 7, 'Action Required', 'Please attach your bank deposit slip on ticket TCK-2026-019.', 'TICKET_REPLIED', 19, 0, DATE_SUB(NOW(), INTERVAL 70 HOUR)),
(28, 8, 'Ticket Closed', 'Ticket TCK-2026-028 has been marked as CLOSED.', 'TICKET_CLOSED', 28, 1, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(29, 9, 'Refund Update', 'Emergency wire batch refund initiated for ticket TCK-2026-024.', 'TICKET_STATUS_CHANGED', 24, 0, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(30, 10, 'Refund Credited', 'Refund of $125.00 for damaged headset completed successfully.', 'TICKET_STATUS_CHANGED', 29, 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(31, 11, 'Replacement Dispatched', 'Replacement adapter dispatched under waybill TRK-LK-77610.', 'TICKET_STATUS_CHANGED', 30, 1, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(32, 12, 'Email Update In Progress', 'Corporate billing email update ticket TCK-2026-012 assigned to officer.', 'TICKET_ASSIGNED', 12, 1, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(33, 13, 'Firmware Download Ready', 'Support officer attached firmware patch v3.4.2 on ticket TCK-2026-013.', 'TICKET_REPLIED', 13, 0, DATE_SUB(NOW(), INTERVAL 14 HOUR)),
(34, 14, 'Tracking Issue Resolved', 'Logistics terminal cleared your medical shipment under TCK-2026-032.', 'TICKET_RESOLVED', 32, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(35, 15, 'Tax ID Updated', 'Company tax registration number registered on ticket TCK-2026-033.', 'TICKET_RESOLVED', 33, 1, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(36, 16, '2FA Reset Verified', 'Two-factor security questionnaire approved for ticket TCK-2026-018.', 'TICKET_REPLIED', 18, 0, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(37, 17, 'Proof of Purchase Needed', 'Please upload retail receipt on ticket TCK-2026-023.', 'TICKET_REPLIED', 23, 0, DATE_SUB(NOW(), INTERVAL 68 HOUR)),
(38, 18, 'Surcharge Query Answered', 'Support officer explained remote zone classifications on TCK-2026-034.', 'TICKET_RESOLVED', 34, 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(39, 19, 'Sensor Settings Sent', 'MQTT connection string and SSL certificates provided for TCK-2026-035.', 'TICKET_RESOLVED', 35, 1, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(40, 20, 'Refund Reversal Confirmed', 'Bank reference ARN-882109 issued on ticket TCK-2026-036.', 'TICKET_RESOLVED', 36, 1, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(41, 21, 'Warranty Claim Open', 'Ticket TCK-2026-023 created for replacement power cable.', 'TICKET_CREATED', 23, 1, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(42, 22, 'Billing Ticket Logged', 'Ticket TCK-2026-034 logged for delivery surcharge review.', 'TICKET_CREATED', 34, 1, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(43, 25, 'New Assigned Ticket', 'Ticket TCK-2026-005 assigned to your queue.', 'TICKET_ASSIGNED', 5, 0, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(44, 26, 'New Assigned Ticket', 'Ticket TCK-2026-010 assigned to your queue.', 'TICKET_ASSIGNED', 10, 0, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(45, 27, 'New Assigned Ticket', 'Ticket TCK-2026-013 assigned to your queue.', 'TICKET_ASSIGNED', 13, 0, DATE_SUB(NOW(), INTERVAL 16 HOUR)),
(46, 28, 'New Assigned Ticket', 'Ticket TCK-2026-018 assigned to your queue.', 'TICKET_ASSIGNED', 18, 0, DATE_SUB(NOW(), INTERVAL 14 HOUR)),
(47, 29, 'New Assigned Ticket', 'Ticket TCK-2026-015 assigned to your queue.', 'TICKET_ASSIGNED', 15, 0, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(48, 25, 'Customer Photo Attached', 'Customer uploaded moisture damage proof on ticket TCK-2026-022.', 'TICKET_REPLIED', 22, 0, DATE_SUB(NOW(), INTERVAL 30 HOUR));

-- 15. PASSWORD_RESET_TOKENS
INSERT INTO password_reset_tokens (id, token, user_id, expiry_date, used) VALUES
(1, 'tok_demo_active_sec9821a4f81c72e0b12', 1, DATE_ADD(NOW(), INTERVAL 24 HOUR), 0),
(2, 'tok_demo_active_sec7741b3d92e54a1c88', 6, DATE_ADD(NOW(), INTERVAL 24 HOUR), 0),
(3, 'tok_demo_active_sec6652c4a81f33d7b99', 7, DATE_ADD(NOW(), INTERVAL 12 HOUR), 0),
(4, 'tok_demo_used_sec5541d7e92b11c4a00', 8, DATE_SUB(NOW(), INTERVAL 7 DAY), 1),
(5, 'tok_demo_used_sec4412e8f03c22d5b11', 10, DATE_SUB(NOW(), INTERVAL 15 DAY), 1),
(6, 'tok_demo_expired_sec3321f9a14d33e6c22', 11, DATE_SUB(NOW(), INTERVAL 5 DAY), 0);

-- 16. AUDIT_LOGS
INSERT INTO audit_logs (id, user_id, action, entity_name, entity_id, details, timestamp) VALUES
(1, 1, 'USER_LOGIN', 'User', 1, 'Customer authenticated successfully from IP 192.168.1.101', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(2, 2, 'USER_LOGIN', 'User', 2, 'Support Officer logged in to Agent Console', DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(3, 3, 'USER_LOGIN', 'User', 3, 'Operations Supervisor authenticated', DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(4, 4, 'USER_LOGIN', 'User', 4, 'Customer Support Manager opened Executive Dashboard', DATE_SUB(NOW(), INTERVAL 45 MINUTE)),
(5, 5, 'USER_LOGIN', 'User', 5, 'QA Executive accessed CSAT Analytics Dashboard', DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(6, 1, 'TICKET_CREATE', 'Ticket', 1, 'Created ticket TCK-2026-001 (Missing item)', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(7, 6, 'TICKET_CREATE', 'Ticket', 2, 'Created ticket TCK-2026-002 (Duplicate charge)', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(8, 3, 'TICKET_ASSIGN', 'Ticket', 9, 'Assigned ticket TCK-2026-009 to Demo Support Officer', DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(9, 2, 'STATUS_UPDATE', 'Ticket', 9, 'Changed ticket TCK-2026-009 status to IN_PROGRESS', DATE_SUB(NOW(), INTERVAL 20 HOUR)),
(10, 3, 'TICKET_ASSIGN', 'Ticket', 10, 'Assigned ticket TCK-2026-010 to Kavindi Jayawardena', DATE_SUB(NOW(), INTERVAL 22 HOUR)),
(11, 3, 'SLA_ESCALATION', 'Ticket', 24, 'Ticket TCK-2026-024 escalated to Operations Supervisor due to 14-day SLA breach', DATE_SUB(NOW(), INTERVAL 72 HOUR)),
(12, 4, 'REFUND_APPROVE', 'Ticket', 24, 'Manager authorized emergency wire batch refund WT-90214', DATE_SUB(NOW(), INTERVAL 24 HOUR)),
(13, 3, 'SLA_ESCALATION', 'Ticket', 25, 'Escalated ticket TCK-2026-025 for courier conduct review', DATE_SUB(NOW(), INTERVAL 48 HOUR)),
(14, 26, 'TICKET_RESOLVE', 'Ticket', 26, 'Resolved ticket TCK-2026-026 after VAT invoice upload', DATE_SUB(NOW(), INTERVAL 10 DAY)),
(15, 1, 'FEEDBACK_SUBMIT', 'Feedback', 1, 'Submitted 5-star feedback rating for ticket TCK-2026-026', DATE_SUB(NOW(), INTERVAL 9 DAY)),
(16, 25, 'TICKET_RESOLVE', 'Ticket', 27, 'Resolved ticket TCK-2026-027 after delivery address reroute', DATE_SUB(NOW(), INTERVAL 15 DAY)),
(17, 6, 'FEEDBACK_SUBMIT', 'Feedback', 2, 'Submitted 5-star rating for ticket TCK-2026-027', DATE_SUB(NOW(), INTERVAL 14 DAY)),
(18, 26, 'REFUND_PROCESS', 'Ticket', 29, 'Approved refund credit of $125.00 for damaged headset', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(19, 26, 'TICKET_CLOSE', 'Ticket', 39, 'Closed ticket TCK-2026-039 after customer bank confirmation', DATE_SUB(NOW(), INTERVAL 14 DAY)),
(20, 25, 'TICKET_CLOSE', 'Ticket', 40, 'Closed ticket TCK-2026-040 following weather delay clearance', DATE_SUB(NOW(), INTERVAL 11 DAY)),
(21, 4, 'FAQ_PUBLISH', 'FaqArticle', 1, 'Published FAQ: How can I track my online order in real-time', DATE_SUB(NOW(), INTERVAL 45 DAY)),
(22, 4, 'FAQ_PUBLISH', 'FaqArticle', 2, 'Published FAQ: What is the return policy for damaged items', DATE_SUB(NOW(), INTERVAL 45 DAY)),
(23, 3, 'KB_PUBLISH', 'KnowledgeBaseArticle', 1, 'Published KB Guide: Complete Guide to Real-Time Order Tracking', DATE_SUB(NOW(), INTERVAL 45 DAY)),
(24, 3, 'KB_PUBLISH', 'KnowledgeBaseArticle', 2, 'Published KB Guide: Step-by-Step Return and Refund Procedure', DATE_SUB(NOW(), INTERVAL 45 DAY)),
(25, 5, 'QA_REVIEW', 'Feedback', 27, 'QA Executive initiated service quality audit on ticket TCK-2026-024', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(26, 25, 'USER_LOGIN', 'User', 25, 'Support Officer Nuwan Bandara logged into system', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(27, 26, 'USER_LOGIN', 'User', 26, 'Support Officer Kavindi Jayawardena logged into system', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(28, 27, 'USER_LOGIN', 'User', 27, 'Support Officer Sajith Wickramasinghe logged into system', DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(29, 28, 'USER_LOGIN', 'User', 28, 'Support Officer Nimesha Rathnayake logged into system', DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(30, 29, 'USER_LOGIN', 'User', 29, 'Support Officer Dharshana Abeykoon logged into system', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(31, 3, 'AGENT_STATUS_CHANGE', 'SupportAgent', 3, 'Changed status of Kavindi Jayawardena to BUSY', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(32, 3, 'AGENT_STATUS_CHANGE', 'SupportAgent', 5, 'Changed status of Nimesha Rathnayake to BUSY', DATE_SUB(NOW(), INTERVAL 1 HOUR)),
(33, 4, 'KB_PUBLISH', 'KnowledgeBaseArticle', 6, 'Published SLA Commitments & Priority Matrix guide', DATE_SUB(NOW(), INTERVAL 30 DAY)),
(34, 5, 'QA_REVIEW', 'Feedback', 28, 'QA Executive flagged coupon calculation defect for dev review', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(35, 30, 'SUPERVISOR_AUDIT', 'AuditLog', NULL, 'Operations Assistant ran automated ticket SLA check', DATE_SUB(NOW(), INTERVAL 3 HOUR));
