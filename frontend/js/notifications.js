/**
 * SupportHUB Enterprise Platform - Real-time Notifications Engine (FR08)
 * Handles live unread badge count, dropdown panel, relative time formatting,
 * system event action triggers, role-based routing, and mark-as-read workflows.
 */
const NotificationsEngine = {
  lastUnreadCount: 0,
  pollInterval: null,

  async init() {
    if (!Auth.isLoggedIn()) return;
    await this.fetchUnreadCount(false);
    this.attachEventListeners();
    this.startLivePolling();
  },

  startLivePolling() {
    if (this.pollInterval) clearInterval(this.pollInterval);
    // Poll every 8 seconds for real-time notification alerts
    this.pollInterval = setInterval(() => {
      this.fetchUnreadCount(true);
    }, 8000);
  },

  async fetchUnreadCount(isPoll = false) {
    try {
      const res = await API.get('/notifications/unread-count');
      const counterEl = document.getElementById('notif-counter');
      const count = typeof res.data === 'number' ? res.data : 0;

      if (counterEl) {
        counterEl.textContent = count > 99 ? '99+' : count;
        counterEl.style.display = count > 0 ? 'flex' : 'none';
      }

      // If poll detected new unread notifications, trigger subtle toast alert and update open dropdown
      if (isPoll && count > this.lastUnreadCount && this.lastUnreadCount >= 0) {
        Utils.showToast('🔔 You have new unread notifications!', 'info');
        const dropdown = document.getElementById('notif-dropdown');
        if (dropdown && dropdown.classList.contains('show')) {
          this.fetchDropdownList();
        }
      }

      this.lastUnreadCount = count;
    } catch (err) {
      console.warn('Could not fetch notifications count:', err);
    }
  },

  formatRelativeTime(dateString) {
    if (!dateString) return 'Just now';
    try {
      const date = new Date(dateString);
      if (isNaN(date.getTime())) return 'Recently';

      const now = new Date();
      const diffMs = now - date;
      const diffSec = Math.floor(diffMs / 1000);
      const diffMin = Math.floor(diffSec / 60);
      const diffHour = Math.floor(diffMin / 60);
      const diffDay = Math.floor(diffHour / 24);

      if (diffSec < 45) return 'Just now';
      if (diffMin < 60) return `${diffMin} min ago`;
      if (diffHour === 1) return '1 hour ago';
      if (diffHour < 24) return `${diffHour} hours ago`;
      if (diffDay === 1) return 'Yesterday';
      if (diffDay < 7) return `${diffDay} days ago`;
      return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
    } catch (e) {
      return 'Recently';
    }
  },

  getTypeDetails(type) {
    const t = (type || '').toUpperCase();
    if (t.includes('ASSIGN') || t.includes('CREATE')) {
      return { icon: 'fa-ticket', bg: '#eff6ff', color: '#2563eb' }; // Blue
    }
    if (t.includes('REPLY') || t.includes('COMMENT') || t.includes('MESSAGE')) {
      return { icon: 'fa-comment-dots', bg: '#f0fdf4', color: '#16a34a' }; // Green
    }
    if (t.includes('STATUS') || t.includes('RESOLVED') || t.includes('CLOSED')) {
      return { icon: 'fa-arrows-rotate', bg: '#faf5ff', color: '#9333ea' }; // Purple
    }
    if (t.includes('FEEDBACK') || t.includes('RATING') || t.includes('REVIEW')) {
      return { icon: 'fa-star', bg: '#fefce8', color: '#ca8a04' }; // Amber/Gold
    }
    if (t.includes('BREACH') || t.includes('WARNING') || t.includes('URGENT') || t.includes('SLA')) {
      return { icon: 'fa-triangle-exclamation', bg: '#fef2f2', color: '#dc2626' }; // Red
    }
    return { icon: 'fa-bell', bg: '#f1f5f9', color: '#475569' }; // Slate
  },

  async fetchDropdownList() {
    const dropdown = document.getElementById('notif-dropdown');
    if (!dropdown) return;

    try {
      const res = await API.get('/notifications');
      const notifications = res.data || [];
      const unreadExists = notifications.some(n => !n.read);

      let itemsHtml = '';
      if (!notifications || notifications.length === 0) {
        itemsHtml = `
          <div style="padding: 2.5rem 1rem; text-align: center; color: #94a3b8;">
            <i class="fa-regular fa-bell-slash" style="font-size: 2rem; margin-bottom: 0.6rem; display: block; color: #cbd5e1;"></i>
            <span style="font-size: 0.875rem; font-weight: 500;">No notifications right now</span>
          </div>
        `;
      } else {
        itemsHtml = notifications.slice(0, 7).map(n => {
          const typeDetails = this.getTypeDetails(n.type);
          const isUnread = !n.read;
          const ticketId = n.relatedTicketId || n.referenceId || null;
          const timeStr = this.formatRelativeTime(n.createdAt);

          return `
            <div class="notif-item ${isUnread ? 'unread' : ''}" 
                 onclick="NotificationsEngine.handleNotificationClick(${n.id}, ${ticketId ? `'${ticketId}'` : 'null'}, '${n.type || ''}')">
              <div class="notif-item-icon" style="background: ${typeDetails.bg}; color: ${typeDetails.color};">
                <i class="fa-solid ${typeDetails.icon}"></i>
              </div>
              <div class="notif-item-content">
                <div class="notif-item-header">
                  <span class="notif-item-title" style="color: ${isUnread ? '#0f172a' : '#475569'};">
                    ${isUnread ? '<span style="width: 7px; height: 7px; border-radius: 50%; background: #2563eb; display: inline-block; margin-right: 4px;"></span>' : ''}
                    ${n.title || 'Notification'}
                  </span>
                  <span class="notif-item-time">${timeStr}</span>
                </div>
                <p class="notif-item-msg">${n.message}</p>
              </div>
            </div>
          `;
        }).join('');
      }

      dropdown.innerHTML = `
        <div style="padding: 0.9rem 1.15rem; border-bottom: 1px solid #e2e8f0; font-weight: 700; display: flex; justify-content: space-between; align-items: center; background: #ffffff;">
          <span style="font-size: 0.9rem; color: #0f172a; display: flex; align-items: center; gap: 0.45rem;">
            <i class="fa-solid fa-bell" style="color: #f59e0b;"></i> Notifications
          </span>
          ${unreadExists ? `<button onclick="NotificationsEngine.markAllRead(event)" style="background: none; border: none; color: #2563eb; font-size: 0.775rem; font-weight: 700; cursor: pointer; padding: 0.2rem 0.5rem; border-radius: 6px;" onmouseover="this.style.background='#eff6ff'" onmouseout="this.style.background='none'">Mark all as read</button>` : ''}
        </div>
        <div id="notif-dropdown-list" style="max-height: 350px; overflow-y: auto;">
          ${itemsHtml}
        </div>
        <div style="padding: 0.75rem 1.15rem; text-align: center; border-top: 1px solid #f1f5f9; background: #fafbfc;">
          <a href="javascript:void(0)" onclick="NotificationsEngine.navigateToAll(event)" style="font-size: 0.8rem; font-weight: 700; color: #2563eb; text-decoration: none; display: inline-flex; align-items: center; gap: 0.35rem;">
            View All Notifications <i class="fa-solid fa-arrow-right" style="font-size: 0.75rem;"></i>
          </a>
        </div>
      `;
    } catch (err) {
      console.warn('Could not fetch notifications list:', err);
    }
  },

  async handleNotificationClick(id, ticketId, type) {
    try {
      // Mark as read immediately on click
      if (id) {
        await API.put(`/notifications/${id}/read`).catch(() => API.patch(`/notifications/${id}/read`));
        await this.fetchUnreadCount(false);
      }
      
      const dropdown = document.getElementById('notif-dropdown');
      if (dropdown) dropdown.classList.remove('show');

      // Role-aware context navigation
      const user = Auth.getUser();
      const role = user?.role;
      const basePath = Auth.getBasePath();

      if (ticketId) {
        if (role === 'CUSTOMER') {
          window.location.href = `${basePath}/customer/ticket-details.html?id=${ticketId}`;
          return;
        } else if (role === 'CUSTOMER_SERVICE_OFFICER') {
          window.location.href = `${basePath}/officer/assigned-tickets.html?id=${ticketId}`;
          return;
        } else if (role === 'OPERATIONS_SUPERVISOR') {
          window.location.href = `${basePath}/supervisor/team-tickets.html?id=${ticketId}`;
          return;
        }
      }

      // Default role landing if no specific ticket
      if (type && type.includes('FEEDBACK') && role === 'QA_EXECUTIVE') {
        window.location.href = `${basePath}/qa/feedback-review.html`;
        return;
      }

      await this.fetchDropdownList();
    } catch (err) {
      console.warn('Error handling notification click:', err);
    }
  },

  async markAllRead(e) {
    if (e) e.stopPropagation();
    try {
      await API.put('/notifications/read-all').catch(() => API.patch('/notifications/read-all'));
      Utils.showToast('All notifications marked as read', 'success');
      await this.fetchUnreadCount(false);
      await this.fetchDropdownList();
    } catch (err) {
      Utils.showToast('Failed to mark notifications as read', 'error');
    }
  },

  navigateToAll(e) {
    if (e) e.stopPropagation();
    const dropdown = document.getElementById('notif-dropdown');
    if (dropdown) dropdown.classList.remove('show');

    const user = Auth.getUser();
    const role = user?.role;
    const basePath = Auth.getBasePath();

    if (role === 'CUSTOMER') {
      window.location.href = `${basePath}/customer/my-tickets.html`;
    } else if (role === 'CUSTOMER_SERVICE_OFFICER') {
      window.location.href = `${basePath}/officer/assigned-tickets.html`;
    } else if (role === 'OPERATIONS_SUPERVISOR') {
      window.location.href = `${basePath}/supervisor/team-tickets.html`;
    } else if (role === 'QA_EXECUTIVE') {
      window.location.href = `${basePath}/qa/qa-dashboard.html`;
    } else if (role === 'CUSTOMER_SUPPORT_MANAGER') {
      window.location.href = `${basePath}/manager/manager-dashboard.html`;
    } else {
      window.location.href = `${basePath}/index.html`;
    }
  },

  attachEventListeners() {
    const bellBtn = document.getElementById('notif-bell');
    const dropdown = document.getElementById('notif-dropdown');

    if (bellBtn && dropdown) {
      bellBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        dropdown.classList.toggle('show');
        if (dropdown.classList.contains('show')) {
          this.fetchDropdownList();
        }
      });

      document.addEventListener('click', (e) => {
        if (!dropdown.contains(e.target) && !bellBtn.contains(e.target)) {
          dropdown.classList.remove('show');
        }
      });
    }
  }
};
