/**
 * SupportHUB Enterprise Platform - Authentication & RBAC Guard
 */
const Auth = {
  getToken() {
    return localStorage.getItem('jwt_token');
  },

  getUser() {
    const raw = localStorage.getItem('user_info');
    return raw ? JSON.parse(raw) : null;
  },

  isLoggedIn() {
    return !!this.getToken();
  },

  setSession(token, user) {
    localStorage.setItem('jwt_token', token);
    localStorage.setItem('user_info', JSON.stringify(user));
  },

  getBasePath() {
    const path = window.location.pathname;
    if (path.includes('/frontend/')) {
      return path.substring(0, path.indexOf('/frontend/') + 9);
    }
    return '';
  },

  logout() {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_info');
    window.location.href = `${this.getBasePath()}/login.html`;
  },

  requireRole(allowedRoles) {
    if (!this.isLoggedIn()) {
      window.location.href = `${this.getBasePath()}/login.html`;
      return false;
    }

    const user = this.getUser();
    const currentPath = window.location.pathname;

    // Specifically block Supervisor from accessing Manager Overview and QA Dashboard as requested
    if (user && user.role === 'OPERATIONS_SUPERVISOR') {
      if (currentPath.includes('manager-dashboard.html') || currentPath.includes('qa-dashboard.html')) {
        window.location.href = `${this.getBasePath()}/supervisor/supervisor-dashboard.html`;
        return false;
      }
      return true;
    }

    if (!user || !allowedRoles.includes(user.role)) {
      window.location.href = `${this.getBasePath()}/unauthorized.html`;
      return false;
    }
    return true;
  },

  renderUserHeader() {
    const user = this.getUser();
    if (!user) return;

    const nameEl = document.getElementById('header-user-name');
    const roleEl = document.getElementById('header-user-role');
    const avatarEl = document.getElementById('header-user-avatar');

    if (nameEl) nameEl.textContent = user.fullName || user.email;
    if (roleEl) roleEl.textContent = user.role.replaceAll('_', ' ');
    if (avatarEl) {
      const initials = (user.fullName || user.email).substring(0, 2).toUpperCase();
      avatarEl.textContent = initials;
    }
  },

  renderSidebar() {
    const user = this.getUser();
    const navContainer = document.getElementById('sidebar-nav');
    if (!navContainer) return;

    const basePath = this.getBasePath();
    const currentPath = window.location.pathname;

    let links = [];

    if (!user) {
      links = [
        { label: 'Home', url: '/index.html', icon: '<i class="fa-solid fa-house"></i>' },
        { label: 'Knowledge Base', url: '/customer/knowledge-base.html', icon: '<i class="fa-solid fa-book-open-reader"></i>' },
        { label: 'Sign In', url: '/login.html', icon: '<i class="fa-solid fa-right-to-bracket"></i>' },
        { label: 'Register', url: '/register.html', icon: '<i class="fa-solid fa-user-plus"></i>' }
      ];
    } else {
      switch (user.role) {
        case 'CUSTOMER':
          links = [
            { label: 'Dashboard', url: '/customer/customer-dashboard.html', icon: '<i class="fa-solid fa-chart-pie"></i>' },
            { label: 'My Tickets', url: '/customer/my-tickets.html', icon: '<i class="fa-solid fa-ticket-simple"></i>' },
            { label: 'Knowledge Base', url: '/customer/knowledge-base.html', icon: '<i class="fa-solid fa-book-open-reader"></i>' },
            { label: 'My Profile', url: '/customer/customer-profile.html', icon: '<i class="fa-solid fa-user-gear"></i>' }
          ];
          break;

        case 'CUSTOMER_SERVICE_OFFICER':
          links = [
            { section: 'OFFICER PANEL' },
            { label: 'Dashboard', url: '/officer/officer-dashboard.html', icon: '<i class="fa-solid fa-chart-line"></i>' },
            { label: 'Assigned Queue', url: '/officer/assigned-tickets.html', icon: '<i class="fa-solid fa-list-check"></i>' },
            { label: 'Knowledge Base', url: '/customer/knowledge-base.html', icon: '<i class="fa-solid fa-book-bookmark"></i>' }
          ];
          break;

        case 'OPERATIONS_SUPERVISOR':
          links = [
            { section: 'ADMINISTRATION' },
            { label: 'Supervisor Dashboard', url: '/supervisor/supervisor-dashboard.html', icon: '<i class="fa-solid fa-chart-line"></i>' },
            { label: 'Customers', url: '/supervisor/customers.html', icon: '<i class="fa-solid fa-users"></i>' },
            { label: 'Support Officers', url: '/supervisor/agents.html', icon: '<i class="fa-solid fa-users-gear"></i>' },
            { label: 'Ticket Categories', url: '/supervisor/categories.html', icon: '<i class="fa-solid fa-folder-tree"></i>' },

            { section: 'TICKET OPERATIONS' },
            { label: 'Team Ticket Queue', url: '/supervisor/team-tickets.html', icon: '<i class="fa-solid fa-list-check"></i>' },

            { section: 'KNOWLEDGE & CONTENT' },
            { label: 'Knowledge Base', url: '/manager/knowledge-base-management.html', icon: '<i class="fa-solid fa-book-bookmark"></i>' },
            { label: 'FAQs Management', url: '/manager/faqs-management.html', icon: '<i class="fa-solid fa-circle-question"></i>' },

            { section: 'QUALITY ASSURANCE' },
            { label: 'Feedback Review', url: '/qa/feedback-review.html', icon: '<i class="fa-solid fa-comments"></i>' },
            { label: 'CSAT Analytics', url: '/qa/satisfaction-analysis.html', icon: '<i class="fa-solid fa-chart-column"></i>' }
          ];
          break;

        case 'CUSTOMER_SUPPORT_MANAGER':
          links = [
            { section: 'MANAGEMENT' },
            { label: 'Dashboard', url: '/manager/manager-dashboard.html', icon: '<i class="fa-solid fa-gauge-high"></i>' },
            { label: 'Tickets', url: '/supervisor/team-tickets.html', icon: '<i class="fa-solid fa-ticket"></i>' },
            { label: 'Customers', url: '/supervisor/customers.html', icon: '<i class="fa-solid fa-users"></i>' },
            { label: 'Support Officers', url: '/supervisor/agents.html', icon: '<i class="fa-solid fa-user-tie"></i>' },
            { label: 'Ticket Categories', url: '/supervisor/categories.html', icon: '<i class="fa-solid fa-folder-tree"></i>' },

            { section: 'KNOWLEDGE & CONTENT' },
            { label: 'FAQs Management', url: '/manager/faqs-management.html', icon: '<i class="fa-solid fa-circle-question"></i>' },
            { label: 'Knowledge Base', url: '/manager/knowledge-base-management.html', icon: '<i class="fa-solid fa-book-bookmark"></i>' },

            { section: 'REPORTS & ANALYTICS' },
            { label: 'CSAT Analytics', url: '/qa/satisfaction-analysis.html', icon: '<i class="fa-solid fa-chart-line"></i>' },
            { label: 'Feedback Review', url: '/qa/feedback-review.html', icon: '<i class="fa-solid fa-comments"></i>' }
          ];
          break;

        case 'QA_EXECUTIVE':
          links = [
            { section: 'QA EXECUTIVE' },
            { label: 'Dashboard', url: '/qa/qa-dashboard.html', icon: '<i class="fa-solid fa-chart-pie"></i>' },
            { label: 'Feedback Review', url: '/qa/feedback-review.html', icon: '<i class="fa-solid fa-comments"></i>' },
            { label: 'CSAT Analytics', url: '/qa/satisfaction-analysis.html', icon: '<i class="fa-solid fa-chart-column"></i>' }
          ];
          break;

        default:
          break;
      }
    }

    navContainer.innerHTML = links.map(link => {
      if (link.section) {
        return `
          <div class="sidebar-section-header">
            <span>${link.section}</span>
          </div>
        `;
      }
      const fullUrl = `${basePath}${link.url}`;
      const isActive = currentPath.endsWith(link.url);
      return `
        <a href="${fullUrl}" class="nav-item ${isActive ? 'active' : ''}">
          <span class="nav-icon">${link.icon}</span>
          <span class="nav-label">${link.label}</span>
          ${isActive ? '<span class="active-indicator-dot"></span>' : ''}
        </a>
      `;
    }).join('');

    // Ensure sleek sidebar user profile footer
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) {
      let footer = document.getElementById('sidebar-user-footer');
      if (!footer) {
        footer = document.createElement('div');
        footer.id = 'sidebar-user-footer';
        footer.className = 'sidebar-user-footer';
        sidebar.appendChild(footer);
      }

      if (user) {
        const initials = (user.fullName || user.email).substring(0, 2).toUpperCase();
        const roleMap = {
          'CUSTOMER_SUPPORT_MANAGER': 'Support Manager',
          'OPERATIONS_SUPERVISOR': 'Operations Supervisor',
          'CUSTOMER_SERVICE_OFFICER': 'Support Officer',
          'QA_EXECUTIVE': 'QA Executive',
          'CUSTOMER': 'Customer'
        };
        const displayRole = roleMap[user.role] || user.role.replaceAll('_', ' ');

        footer.innerHTML = `
          <div class="sidebar-user-card">
            <div class="sidebar-avatar-wrap">
              <span class="sidebar-avatar">${initials}</span>
              <span class="online-status-dot"></span>
            </div>
            <div class="sidebar-user-meta">
              <span class="sidebar-user-name">${user.fullName || user.email}</span>
              <span class="sidebar-user-role">${displayRole}</span>
            </div>
          </div>
        `;
      } else {
        footer.innerHTML = `
          <div class="sidebar-user-card" style="cursor: pointer;" onclick="window.location.href='${basePath}/login.html'">
            <div class="sidebar-avatar-wrap">
              <span class="sidebar-avatar" style="background: #e2e8f0; color: #475569;"><i class="fa-solid fa-user"></i></span>
            </div>
            <div class="sidebar-user-meta">
              <span class="sidebar-user-name">Guest Visitor</span>
              <span class="sidebar-user-role" style="color: #2563eb; font-weight: 700;">Sign In →</span>
            </div>
          </div>
        `;
      }
    }

    // Auto initialize SupportHUB Virtual Support Assistant for Customers
    if (user.role === 'CUSTOMER') {
      if (typeof ChatbotAssistant !== 'undefined') {
        ChatbotAssistant.init();
      } else {
        const existingScript = document.querySelector('script[src*="chatbot.js"]');
        if (!existingScript) {
          const script = document.createElement('script');
          script.src = `${basePath}/js/chatbot.js`;
          script.onload = () => {
            if (typeof ChatbotAssistant !== 'undefined') {
              ChatbotAssistant.init();
            }
          };
          document.head.appendChild(script);
        }
      }
    }
  },

  fixUrlSpace() {
    if (window.location.pathname.includes('%20') || window.location.pathname.includes(' ')) {
      const cleanPath = window.location.pathname.replace(/%20|\s+/g, '-');
      window.location.replace(cleanPath + window.location.search + window.location.hash);
    }
  }
};

// Instantly auto-fix URLs with spaces
Auth.fixUrlSpace();
