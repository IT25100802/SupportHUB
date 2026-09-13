/**
 * SupportHUB Enterprise Platform - Real-time Notifications Engine
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
    // Poll every 6 seconds for real-time notification alerts
    this.pollInterval = setInterval(() => {
      this.fetchUnreadCount(true);
    }, 6000);
  },

  async fetchUnreadCount(isPoll = false) {
    try {
      const res = await API.get('/notifications/unread-count');
      const counterEl = document.getElementById('notif-counter');
      const count = typeof res.data === 'number' ? res.data : 0;

      if (counterEl) {
        counterEl.textContent = count;
        counterEl.style.display = count > 0 ? 'flex' : 'none';
      }

      // If poll detected new unread notifications, trigger subtle toast alert
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
          <div style="padding: 2rem 1rem; text-align: center; color: #94a3b8; font-size: 0.875rem;">
            <i class="fa-regular fa-bell-slash" style="font-size: 1.75rem; margin-bottom: 0.5rem; display: block; color: #cbd5e1;"></i>
            No notifications yet
          </div>
        `;
      } else {
        itemsHtml = notifications.slice(0, 8).map(n => `
          <div class="notif-item ${n.read ? '' : 'unread'}" onclick="NotificationsEngine.handleNotificationClick(${n.id}, ${n.relatedTicketId || 'null'})" style="padding: 0.85rem 1.15rem; border-bottom: 1px solid #f1f5f9; cursor: pointer; transition: background 0.15s ease; ${n.read ? 'background: #ffffff;' : 'background: #f0f9ff; font-weight: 600;'}">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 0.25rem;">
              <span style="font-size: 0.85rem; font-weight: 700; color: ${n.read ? '#334155' : '#1e40af'};">${n.title || 'Notification'}</span>
              ${!n.read ? '<span style="width: 8px; height: 8px; border-radius: 50%; background: #2563eb; display: inline-block;"></span>' : ''}
            </div>
            <div style="color: #475569; font-size: 0.8rem; line-height: 1.4; margin-bottom: 0.35rem;">${n.message}</div>
            <div style="font-size: 0.7rem; color: #94a3b8; display: flex; align-items: center; gap: 0.3rem;">
              <i class="fa-regular fa-clock"></i> ${Utils.formatDate(n.createdAt)}
            </div>
          </div>
        `).join('');
      }

      dropdown.innerHTML = `
        <div style="padding: 0.85rem 1.15rem; border-bottom: 1px solid #e2e8f0; font-weight: 700; display: flex; justify-content: space-between; align-items: center; background: #ffffff; border-radius: 12px 12px 0 0;">
          <span style="font-size: 0.9rem; color: #0f172a; display: flex; align-items: center; gap: 0.4rem;">
            <i class="fa-solid fa-bell" style="color: #2563eb;"></i> Notifications
          </span>
          ${unreadExists ? `<button onclick="NotificationsEngine.markAllRead(event)" style="background: none; border: none; color: #2563eb; font-size: 0.775rem; font-weight: 700; cursor: pointer; padding: 0.2rem 0.5rem; border-radius: 6px;" onmouseover="this.style.background='#eff6ff'" onmouseout="this.style.background='none'">✓ Mark all as read</button>` : ''}
        </div>
        <div id="notif-dropdown-list" style="max-height: 360px; overflow-y: auto;">
          ${itemsHtml}
        </div>
      `;
    } catch (err) {
      console.warn('Could not fetch notifications list:', err);
    }
  },

  async handleNotificationClick(id, ticketId) {
    try {
      await API.put(`/notifications/${id}/read`);
      await this.fetchUnreadCount(false);
      
      const dropdown = document.getElementById('notif-dropdown');
      if (dropdown) dropdown.classList.remove('show');

      if (ticketId) {
        const basePath = Auth.getBasePath();
        window.location.href = `${basePath}/customer/ticket-details.html?id=${ticketId}`;
      } else {
        await this.fetchDropdownList();
      }
    } catch (err) {
      console.warn('Error handling notification click:', err);
    }
  },

  async markAllRead(e) {
    if (e) e.stopPropagation();
    try {
      await API.put('/notifications/read-all');
      Utils.showToast('All notifications marked as read', 'success');
      await this.fetchUnreadCount(false);
      await this.fetchDropdownList();
    } catch (err) {
      Utils.showToast('Failed to mark notifications as read', 'error');
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
