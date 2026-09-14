/**
 * SupportHUB Enterprise Platform - Utility & UI Component Helpers
 */
const Utils = {
  escapeHtml(str) {
    if (str === null || str === undefined) return '';
    return String(str)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
  },

  formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  },

  getStatusBadge(status) {
    if (!status) return '';
    const formatted = status.replaceAll('_', ' ');
    return `<span class="badge badge-${status.toLowerCase()}">${formatted}</span>`;
  },

  getPriorityBadge(priority) {
    if (!priority) return '';
    return `<span class="badge badge-${priority.toLowerCase()}">${priority}</span>`;
  },

  renderStars(rating) {
    let stars = '';
    for (let i = 1; i <= 5; i++) {
      stars += i <= rating ? '★' : '☆';
    }
    return `<span style="color: #f59e0b; font-size: 1.1rem;">${stars}</span>`;
  },

  showToast(message, type = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
      container = document.createElement('div');
      container.id = 'toast-container';
      document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.innerHTML = `
      <span>${message}</span>
      <button onclick="this.parentElement.remove()" style="background:none;border:none;color:#fff;cursor:pointer;">&times;</button>
    `;

    container.appendChild(toast);

    setTimeout(() => {
      if (toast.parentElement) toast.remove();
    }, 4000);
  },

  initLiveClock() {
    const dateEl = document.getElementById('live-date-text');
    const timeEl = document.getElementById('live-time-text');
    if (!dateEl && !timeEl) return;

    function update() {
      const now = new Date();
      const dateStr = now.toLocaleDateString('en-US', { weekday: 'short', day: '2-digit', month: 'short', year: 'numeric' });
      const timeStr = now.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true });

      if (dateEl) dateEl.textContent = dateStr;
      if (timeEl) timeEl.textContent = timeStr;
    }

    update();
    setInterval(update, 1000);
  }
};

document.addEventListener('DOMContentLoaded', () => {
  Utils.initLiveClock();
});
