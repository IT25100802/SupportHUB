# Web-Based Customer Support System - SE2030 Group Project

**Module**: SE2030 - Software Engineering  
**Academic Level**: BSc (Hons) in Information Technology (Year 2 Semester 1)  
**Technology Stack**: Java 17, Spring Boot 3.2.3, Spring Security (JWT), Spring Data JPA, MySQL 8.0, Vanilla HTML5 / CSS3 / Vanilla JavaScript (Fetch API).

---

## 1. Project Overview
The Web-Based Customer Support System is a complete academic web application featuring role-based access control (5 roles), ticket workflow management, support category mapping, automated FAQ chatbot assistant using the **Strategy Pattern**, persistent notifications, and customer satisfaction (CSAT) quality reporting using the **Factory Pattern**.

---

## 2. Pre-configured Demo Credentials (For Evaluation)

All account passwords are set to their respective role credentials below (BCrypt hashed in database):

| Role | Email | Password | Allowed Access |
| :--- | :--- | :--- | :--- |
| **CUSTOMER** | `customer@demo.com` | `Customer123!` | Dashboard, Create Ticket, My Tickets, FAQs, KB, Chatbot, Submit Feedback |
| **CUSTOMER_SERVICE_OFFICER** | `officer@demo.com` | `Officer123!` | Officer Dashboard, Assigned Tickets Queue, Status Transitions, Internal Notes |
| **OPERATIONS_SUPERVISOR** | `supervisor@demo.com` | `Supervisor123!` | Supervisor Dashboard, Ticket Categories, Officers Roster, Category Assignments |
| **CUSTOMER_SUPPORT_MANAGER** | `manager@demo.com` | `Manager123!` | Manager Dashboard, FAQs Management, Knowledge Base Publishing |
| **QA_EXECUTIVE** | `qa@demo.com` | `QaExecutive123!` | QA Dashboard, Customer Feedback Log Review, CSAT Analytics |

---

## 3. Database Setup Instructions (MySQL 8.0)

1. Ensure MySQL Server 8.0 is running locally on port `3306`.
2. Open MySQL CLI / MySQL Workbench and execute the setup scripts:

```bash
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS customer_support_db;"
mysql -u root -p customer_support_db < database/schema.sql
mysql -u root -p customer_support_db < database/seed.sql
```

> **Note**: Database connection parameters are pre-configured in [`backend/src/main/resources/application.properties`](file:///c:/Users/Chithu/Desktop/SE/backend/src/main/resources/application.properties) (`username=root`, `password=root`).

---

## 4. Running Backend in IntelliJ IDEA

1. Open **IntelliJ IDEA**.
2. Click **File -> Open...** and select the [`backend`](file:///c:/Users/Chithu/Desktop/SE/backend) directory (or the main root project folder).
3. Wait for IntelliJ to load the Maven dependencies automatically.
4. Locate the main class:
   - Path: [`backend/src/main/java/com/example/customersupport/CustomerSupportApplication.java`](file:///c:/Users/Chithu/Desktop/SE/backend/src/main/java/com/example/customersupport/CustomerSupportApplication.java)
5. Click the green **Run ▶** button next to `main()` or select the pre-configured **CustomerSupportApplication** run configuration at the top right and press `Shift + F10` / click **Run**.
6. The REST API server will start on `http://localhost:8080`.

*(Alternatively via terminal: `cd backend` -> `mvn spring-boot:run`)*

---

## 5. Running Frontend in VS Code

1. Open **VS Code**.
2. Click **File -> Open Folder...** and select the [`SE`](file:///c:/Users/Chithu/Desktop/SE) workspace or [`frontend`](file:///c:/Users/Chithu/Desktop/SE/frontend) directory.
3. Install the recommended **Live Server** extension (`ritwickdey.liveserver`) if not already installed.
4. Right-click [`frontend/login.html`](file:///c:/Users/Chithu/Desktop/SE/frontend/login.html) and select **Open with Live Server** (or press `Alt + L, Alt + O`).
5. Alternatively, open VS Code Terminal (`Ctrl + \``) and run:
   ```bash
   npx -y serve -l 3000 frontend
   ```
6. Open your browser at `http://localhost:3000/login.html`.
