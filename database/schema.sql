-- SE2030 Customer Support System Database Schema
-- Target Database: MySQL 8.0+

CREATE DATABASE IF NOT EXISTS customer_support_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE customer_support_db;

-- 2. Customers Table
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_customer_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Support Agents Table
CREATE TABLE IF NOT EXISTS support_agents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    employee_code VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_agent_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Ticket Categories Table
CREATE TABLE IF NOT EXISTS ticket_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. FAQ Articles Table
CREATE TABLE IF NOT EXISTS faq_articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(255) NOT NULL,
    answer TEXT NOT NULL,
    category_id BIGINT NULL,
    is_published TINYINT(1) NOT NULL DEFAULT 1,
    keywords TEXT,
    view_count INT NOT NULL DEFAULT 0,
    created_by_id BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_faq_category FOREIGN KEY (category_id) REFERENCES ticket_categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_faq_user FOREIGN KEY (created_by_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 12. Knowledge Base Articles Table
CREATE TABLE IF NOT EXISTS knowledge_base_articles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content LONGTEXT NOT NULL,
    category_id BIGINT NULL,
    is_published TINYINT(1) NOT NULL DEFAULT 1,
    tags VARCHAR(255),
    view_count INT NOT NULL DEFAULT 0,
    created_by_id BIGINT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_kb_category FOREIGN KEY (category_id) REFERENCES ticket_categories(id) ON DELETE SET NULL,
    CONSTRAINT fk_kb_user FOREIGN KEY (created_by_id) REFERENCES users(id) ON DELETE SET NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. Feedback Table
CREATE TABLE IF NOT EXISTS feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    suggestions TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_feedback_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    CONSTRAINT fk_feedback_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Support Agent Categories Mapping Table (Many-To-Many)
CREATE TABLE IF NOT EXISTS support_agent_categories (
    agent_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (agent_id, category_id),
    CONSTRAINT fk_sac_agent FOREIGN KEY (agent_id) REFERENCES support_agents(id) ON DELETE CASCADE,
    CONSTRAINT fk_sac_category FOREIGN KEY (category_id) REFERENCES ticket_categories(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
