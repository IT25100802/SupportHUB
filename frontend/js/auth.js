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
    if (!navContainer || !user) return;

    const basePath = this.getBasePath();
    const currentPath = window.location.pathname;

    let links = [];

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
          { label: 'Dashboard', url: '/officer/officer-dashboard.html', icon: '<i class="fa-solid fa-chart-line"></i>' },
          { label: 'Assigned Queue', url: '/officer/assigned-tickets.html', icon: '<i class="fa-solid fa-list-check"></i>' }
        ];
        break;

      case 'OPERATIONS_SUPERVISOR':
        links = [
          { label: 'Dashboard', url: '/supervisor/supervisor-dashboard.html', icon: '<i class="fa-solid fa-chart-line"></i>' },
          { label: 'Ticket Categories', url: '/supervisor/categories.html', icon: '<i class="fa-solid fa-folder-tree"></i>' },
          { label: 'Support Officers', url: '/supervisor/agents.html', icon: '<i class="fa-solid fa-users-gear"></i>' }
        ];
        break;

      case 'CUSTOMER_SUPPORT_MANAGER':
        links = [
          { label: 'Dashboard', url: '/manager/manager-dashboard.html', icon: '<i class="fa-solid fa-gauge-high"></i>' },
          { label: 'FAQs Management', url: '/manager/faqs-management.html', icon: '<i class="fa-solid fa-circle-question"></i>' },
          { label: 'Knowledge Base', url: '/manager/knowledge-base-management.html', icon: '<i class="fa-solid fa-book-bookmark"></i>' }
        ];
        break;

      case 'QA_EXECUTIVE':
        links = [
          { label: 'Dashboard', url: '/qa/qa-dashboard.html', icon: '<i class="fa-solid fa-chart-pie"></i>' },
          { label: 'Feedback Review', url: '/qa/feedback-review.html', icon: '<i class="fa-solid fa-comments"></i>' },
          { label: 'CSAT Analytics', url: '/qa/satisfaction-analysis.html', icon: '<i class="fa-solid fa-chart-column"></i>' }
        ];
        break;

      default:
        break;
    }

    navContainer.innerHTML = links.map(link => {
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

    // Inject Floating AI Chatbot Button for Customers
    if (user.role === 'CUSTOMER') {
      let floatBtn = document.getElementById('floating-chatbot-btn');
      if (!floatBtn) {
        floatBtn = document.createElement('a');
        floatBtn.id = 'floating-chatbot-btn';
        floatBtn.className = 'floating-chatbot-btn';
        floatBtn.href = `${basePath}/customer/chatbot.html`;
        floatBtn.title = 'Chat with AI Support Assistant';
        floatBtn.innerHTML = `
          <span class="chat-pulse-dot"></span>
          <i class="fa-solid fa-robot"></i>
          <span class="floating-chatbot-tooltip">Ask AI Assistant 🤖</span>
        `;
        document.body.appendChild(floatBtn);
      }
    }

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

      const initials = (user.fullName || user.email).substring(0, 2).toUpperCase();
      const roleMap = {
        'CUSTOMER_SUPPORT_MANAGER': 'Support Manager',
        'OPERATIONS_SUPERVISOR': 'Operations Supervisor',
        'CUSTOMER_SERVICE_OFFICER': 'Support Officer',
        'QA_EXECUTIVE': 'QA Executive',
        'CUSTOMER': 'Verified Customer'
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
