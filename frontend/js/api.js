/**
 * SupportHUB Enterprise Platform - Centralized API Client
 */
const API_BASE_URL = 'http://localhost:8080/api';

function getAppBasePath() {
  const path = window.location.pathname;
  if (path.includes('/frontend/')) {
    return path.substring(0, path.indexOf('/frontend/') + 9);
  }
  return '';
}

const API = {
  async request(endpoint, options = {}) {
    const token = localStorage.getItem('jwt_token');
    
    const headers = {
      'Accept': 'application/json',
      ...options.headers
    };

    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    if (!(options.body instanceof FormData) && !headers['Content-Type']) {
      headers['Content-Type'] = 'application/json';
    }

    const config = {
      ...options,
      headers
    };

    try {
      const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
      let data = {};
      try {
        data = await response.json();
      } catch (e) {
        data = { message: response.statusText || `HTTP ${response.status}` };
      }

      if (response.status === 401) {
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_info');
        if (!window.location.pathname.endsWith('login.html') && !window.location.pathname.endsWith('index.html')) {
          window.location.href = `${getAppBasePath()}/login.html?expired=true`;
        }
        throw new Error(data.message || 'Session expired. Please log in again.');
      }

      if (response.status === 403) {
        if (!window.location.pathname.endsWith('unauthorized.html')) {
          window.location.href = `${getAppBasePath()}/unauthorized.html`;
        }
        throw new Error(data.message || 'Access Denied');
      }

      if (!response.ok || data.success === false) {
        throw new Error(data.message || `HTTP Error ${response.status}`);
      }

      return data;
    } catch (err) {
      console.error(`API Error [${endpoint}]:`, err);
      throw err;
    }
  },

  get(endpoint) {
    return this.request(endpoint, { method: 'GET' });
  },

  post(endpoint, body) {
    return this.request(endpoint, {
      method: 'POST',
      body: body instanceof FormData ? body : JSON.stringify(body)
    });
  },

  put(endpoint, body) {
    return this.request(endpoint, {
      method: 'PUT',
      body: body instanceof FormData ? body : JSON.stringify(body)
    });
  },

  patch(endpoint, body) {
    return this.request(endpoint, {
      method: 'PATCH',
      body: body ? (body instanceof FormData ? body : JSON.stringify(body)) : null
    });
  },

  delete(endpoint) {
    return this.request(endpoint, { method: 'DELETE' });
  }
};
