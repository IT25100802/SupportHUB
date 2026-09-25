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
    const dateEls = document.querySelectorAll('#live-date-text, .live-date-text');
    const timeEls = document.querySelectorAll('#live-time-text, .live-time-text');
    const comboEls = document.querySelectorAll('#live-datetime-text, .live-datetime-text');
    if (dateEls.length === 0 && timeEls.length === 0 && comboEls.length === 0) return;

    function update() {
      const now = new Date();
      const dateStr = now.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: '2-digit', year: 'numeric' });
      const timeStr = now.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true });

      dateEls.forEach(el => { el.textContent = dateStr; });
      timeEls.forEach(el => { el.textContent = timeStr; });
      comboEls.forEach(el => { el.textContent = `${dateStr} | ${timeStr}`; });
    }

    update();
    setInterval(update, 1000);
  }
};

/**
 * Universal Form Validation Helper
 */
const Validation = {
  // Regex Patterns
  patterns: {
    personName: /^[a-zA-Z\s.'-]+$/,
    hasDigits: /\d/,
    email: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    phone: /^[0-9+\s()\-]{7,20}$/,
    alphanumericCode: /^[a-zA-Z0-9\-_]+$/
  },

  validatePersonName(name, fieldName = 'Full name') {
    if (!name || typeof name !== 'string' || !name.trim()) {
      return { valid: false, message: `${fieldName} is required` };
    }
    const trimmed = name.trim();
    if (this.patterns.hasDigits.test(trimmed)) {
      return { valid: false, message: `${fieldName} cannot contain numbers (letters only)` };
    }
    if (!this.patterns.personName.test(trimmed)) {
      return { valid: false, message: `${fieldName} can only contain letters, spaces, hyphens, and apostrophes` };
    }
    if (trimmed.length < 2) {
      return { valid: false, message: `${fieldName} must be at least 2 characters long` };
    }
    if (trimmed.length > 100) {
      return { valid: false, message: `${fieldName} cannot exceed 100 characters` };
    }
    return { valid: true };
  },

  validateEmail(email, fieldName = 'Email') {
    if (!email || !email.trim()) {
      return { valid: false, message: `${fieldName} is required` };
    }
    const trimmed = email.trim();
    if (!this.patterns.email.test(trimmed)) {
      return { valid: false, message: `Please enter a valid email address (e.g. name@domain.com)` };
    }
    return { valid: true };
  },

  validatePhone(phone, fieldName = 'Phone number', required = false) {
    if (!phone || !phone.trim()) {
      if (required) return { valid: false, message: `${fieldName} is required` };
      return { valid: true };
    }
    const trimmed = phone.trim();
    if (!this.patterns.phone.test(trimmed)) {
      return { valid: false, message: `Please enter a valid phone number (7-15 digits)` };
    }
    return { valid: true };
  },

  validatePassword(password, minLength = 6, fieldName = 'Password') {
    if (!password) {
      return { valid: false, message: `${fieldName} is required` };
    }
    if (password.length < minLength) {
      return { valid: false, message: `${fieldName} must be at least ${minLength} characters` };
    }
    return { valid: true };
  },

  validateRequired(value, fieldName = 'Field', minLength = 1) {
    if (value === null || value === undefined || (typeof value === 'string' && !value.trim())) {
      return { valid: false, message: `${fieldName} is required` };
    }
    if (typeof value === 'string' && value.trim().length < minLength) {
      return { valid: false, message: `${fieldName} must be at least ${minLength} characters` };
    }
    return { valid: true };
  },

  validatePositiveNumber(value, fieldName = 'Number', min = 1) {
    if (value === null || value === undefined || value === '') {
      return { valid: false, message: `${fieldName} is required` };
    }
    const num = Number(value);
    if (isNaN(num) || num < min) {
      return { valid: false, message: `${fieldName} must be a number greater than or equal to ${min}` };
    }
    return { valid: true };
  },

  showError(inputEl, message) {
    if (typeof inputEl === 'string') {
      inputEl = document.getElementById(inputEl) || document.querySelector(inputEl);
    }
    if (!inputEl) return;

    inputEl.classList.add('is-invalid');
    inputEl.classList.remove('is-valid');

    // Remove existing error message if present
    const parent = inputEl.parentElement;
    let errorEl = parent.querySelector('.field-error-msg');
    if (!errorEl) {
      errorEl = document.createElement('span');
      errorEl.className = 'field-error-msg';
      parent.appendChild(errorEl);
    }
    errorEl.innerHTML = `<i class="fa-solid fa-circle-exclamation" style="margin-right: 4px;"></i> ${message}`;
  },

  clearError(inputEl, markValid = false) {
    if (typeof inputEl === 'string') {
      inputEl = document.getElementById(inputEl) || document.querySelector(inputEl);
    }
    if (!inputEl) return;

    inputEl.classList.remove('is-invalid');
    if (markValid && inputEl.value && inputEl.value.trim().length > 0) {
      inputEl.classList.add('is-valid');
    } else {
      inputEl.classList.remove('is-valid');
    }

    const parent = inputEl.parentElement;
    const errorEl = parent ? parent.querySelector('.field-error-msg') : null;
    if (errorEl) {
      errorEl.remove();
    }
  },

  resetForm(form) {
    if (typeof form === 'string') {
      form = document.getElementById(form);
    }
    if (!form) return;

    form.querySelectorAll('.is-invalid, .is-valid').forEach(el => {
      el.classList.remove('is-invalid', 'is-valid');
    });
    form.querySelectorAll('.field-error-msg').forEach(el => el.remove());
  },

  // Attach live realtime validations on Person Name inputs
  attachPersonNameLiveValidation(inputEl, fieldName = 'Name') {
    if (typeof inputEl === 'string') {
      inputEl = document.getElementById(inputEl);
    }
    if (!inputEl) return;

    const check = () => {
      const val = inputEl.value;
      if (!val) {
        Validation.clearError(inputEl);
        return;
      }
      const res = Validation.validatePersonName(val, fieldName);
      if (!res.valid) {
        Validation.showError(inputEl, res.message);
      } else {
        Validation.clearError(inputEl, true);
      }
    };

    inputEl.addEventListener('input', check);
    inputEl.addEventListener('blur', check);
  }
};

document.addEventListener('DOMContentLoaded', () => {
  Utils.initLiveClock();
});
