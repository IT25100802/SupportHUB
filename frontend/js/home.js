/**
 * SupportHUB - Enterprise Customer Support System
 * Home Page Interactive Logic (High-Attraction SaaS Level)
 */

document.addEventListener('DOMContentLoaded', () => {
  initNavigation();
  initFAQAccordion();
  initHomeSearch();
  initFeatureFilters();
  initFloatingChatWidget();
  initMockupSimulator();

  loadHomeDynamicContent();
});

/**
 * 1. Mobile Menu Toggle & Smooth Scroll
 */
function initNavigation() {
  const hamburgerBtn = document.getElementById('hamburger-toggle');
  const navMenu = document.getElementById('nav-menu');

  if (hamburgerBtn && navMenu) {
    hamburgerBtn.addEventListener('click', () => {
      navMenu.classList.toggle('active');
    });
  }

  // Active state on scroll & smooth anchor jump
  const navLinks = document.querySelectorAll('.nav-link');
  navLinks.forEach(link => {
    link.addEventListener('click', (e) => {
      const targetId = link.getAttribute('href');
      if (targetId.startsWith('#')) {
        e.preventDefault();
        const targetElement = document.querySelector(targetId);
        if (targetElement) {
          targetElement.scrollIntoView({ behavior: 'smooth' });
          navLinks.forEach(l => l.classList.remove('active'));
          link.classList.add('active');
        }
      }
    });
  });
}

/**
 * 2. FAQ Accordion Collapsible Logic
 */
function initFAQAccordion() {
  const accordionItems = document.querySelectorAll('.accordion-item');

  accordionItems.forEach(item => {
    const header = item.querySelector('.accordion-header');
    if (header) {
      header.addEventListener('click', () => {
        const isActive = item.classList.contains('active');
        
        // Close others for clean single open accordion
        accordionItems.forEach(i => i.classList.remove('active'));
        
        if (!isActive) {
          item.classList.add('active');
        }
      });
    }
  });
}

/**
 * 3. Quick Support Search Filtering & Topic Tag Click
 */
function initHomeSearch() {
  const searchInput = document.getElementById('home-search-input');
  const searchBtn = document.getElementById('home-search-btn');
  const topicTags = document.querySelectorAll('.quick-topic-tag');

  if (searchBtn && searchInput) {
    const handleSearch = (queryOverride) => {
      const query = (queryOverride || searchInput.value).trim().toLowerCase();
      if (!query) return;

      if (queryOverride) {
        searchInput.value = queryOverride;
      }

      const faqSection = document.getElementById('faq');
      if (faqSection) {
        faqSection.scrollIntoView({ behavior: 'smooth' });
      }

      const accordionItems = document.querySelectorAll('.accordion-item');
      let firstMatchFound = false;

      accordionItems.forEach(item => {
        const text = item.textContent.toLowerCase();
        if (text.includes(query)) {
          item.style.display = 'block';
          if (!firstMatchFound) {
            item.classList.add('active');
            firstMatchFound = true;
          }
        } else {
          item.style.display = 'none';
        }
      });
    };

    searchBtn.addEventListener('click', () => handleSearch());
    searchInput.addEventListener('keyup', (e) => {
      if (e.key === 'Enter') handleSearch();
    });

    topicTags.forEach(tag => {
      tag.addEventListener('click', () => {
        const query = tag.getAttribute('data-query');
        if (query) handleSearch(query);
      });
    });
  }
}

/**
 * 4. Interactive Feature Category Filtering
 */
function initFeatureFilters() {
  const filterBtns = document.querySelectorAll('.filter-tab-btn');
  const featureCards = document.querySelectorAll('.feature-card');

  if (!filterBtns.length) return;

  filterBtns.forEach(btn => {
    btn.addEventListener('click', () => {
      filterBtns.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');

      const filterCategory = btn.getAttribute('data-filter');

      featureCards.forEach(card => {
        const cardCat = card.getAttribute('data-category');
        if (filterCategory === 'all' || cardCat === filterCategory) {
          card.style.display = 'flex';
          card.style.opacity = '1';
        } else {
          card.style.display = 'none';
          card.style.opacity = '0';
        }
      });
    });
  });
}

/**
 * 5. Floating Interactive AI Chatbot Widget
 */
function initFloatingChatWidget() {
  const chatLauncher = document.getElementById('floating-chat-launcher');
  const chatModal = document.getElementById('chat-widget-modal');
  const closeChatBtn = document.getElementById('close-chat-widget');
  const sendChatBtn = document.getElementById('widget-send-btn');
  const chatInput = document.getElementById('widget-chat-input');
  const chatBody = document.getElementById('widget-chat-body');

  if (!chatLauncher || !chatModal) return;

  chatLauncher.addEventListener('click', () => {
    chatModal.classList.toggle('open');
  });

  if (closeChatBtn) {
    closeChatBtn.addEventListener('click', () => {
      chatModal.classList.remove('open');
    });
  }

  const handleUserMessage = () => {
    const msgText = chatInput.value.trim();
    if (!msgText) return;

    // Append User Message
    const userDiv = document.createElement('div');
    userDiv.className = 'chat-msg user';
    userDiv.textContent = msgText;
    chatBody.appendChild(userDiv);

    chatInput.value = '';
    chatBody.scrollTop = chatBody.scrollHeight;

    // Simulate AI Assistant Response
    setTimeout(() => {
      const botDiv = document.createElement('div');
      botDiv.className = 'chat-msg bot';
      
      let reply = "SupportHUB AI Assistant is ready to help! You can log in to your account to submit a ticket or explore our Knowledge Base.";
      
      const lower = msgText.toLowerCase();
      if (lower.includes('ticket') || lower.includes('create')) {
        reply = "To create a support ticket, log in to your Customer Portal, click 'Create Ticket', select a category, and click Submit.";
      } else if (lower.includes('status') || lower.includes('track')) {
        reply = "You can view real-time ticket progress under 'My Tickets' in your Customer Dashboard.";
      } else if (lower.includes('login') || lower.includes('register')) {
        reply = "Click the 'Login' or 'Create Account' button at the top right of the page to access your support dashboard.";
      }

      botDiv.textContent = reply;
      chatBody.appendChild(botDiv);
      chatBody.scrollTop = chatBody.scrollHeight;
    }, 600);
  };

  if (sendChatBtn) sendChatBtn.addEventListener('click', handleUserMessage);
  if (chatInput) {
    chatInput.addEventListener('keyup', (e) => {
      if (e.key === 'Enter') handleUserMessage();
    });
  }
}

/**
 * 6. Hero Command Dashboard Simulator
 */
function initMockupSimulator() {
  const toggleBtn = document.getElementById('mockup-toggle-btn');
  const activityList = document.getElementById('mockup-activity-list');

  if (!toggleBtn || !activityList) return;

  let isAgentView = false;

  toggleBtn.addEventListener('click', () => {
    isAgentView = !isAgentView;
    if (isAgentView) {
      toggleBtn.textContent = 'Switch to Customer View';
      activityList.innerHTML = `
        <div style="display: flex; justify-content: space-between; font-size: 0.8rem; background: #ffffff; padding: 0.5rem 0.75rem; border-radius: 4px; border: 1px solid var(--border-color);">
          <span>#TCK-8921 SSO Login Error (Escalated)</span>
          <span class="badge badge-high_priority">High Priority</span>
        </div>
        <div style="display: flex; justify-content: space-between; font-size: 0.8rem; background: #ffffff; padding: 0.5rem 0.75rem; border-radius: 4px; border: 1px solid var(--border-color);">
          <span>#TCK-8920 Payment Timeout (Officer Assigned)</span>
          <span class="badge badge-in_progress">In Progress</span>
        </div>
      `;
    } else {
      toggleBtn.textContent = 'Switch to Officer View';
      activityList.innerHTML = `
        <div style="display: flex; justify-content: space-between; font-size: 0.8rem; background: #ffffff; padding: 0.5rem 0.75rem; border-radius: 4px; border: 1px solid var(--border-color);">
          <span>#TCK-1084 API Access Issue</span>
          <span class="badge badge-open">Open</span>
        </div>
        <div style="display: flex; justify-content: space-between; font-size: 0.8rem; background: #ffffff; padding: 0.5rem 0.75rem; border-radius: 4px; border: 1px solid var(--border-color);">
          <span>#TCK-1082 Account Verification</span>
          <span class="badge badge-resolved">Resolved</span>
        </div>
      `;
    }
  });
}

/**
 * 7. Dynamic API Data Fetcher for Home Page (FAQs & Knowledge Base Articles)
 */
async function loadHomeDynamicContent() {
  const faqListContainer = document.getElementById('home-faq-accordion-list');
  const kbListContainer = document.getElementById('home-kb-cards-list');

  // Load Published FAQs from Backend API
  if (faqListContainer) {
    try {
      const res = await API.get('/faqs/public');
      const faqs = res.data || [];

      if (faqs.length > 0) {
        faqListContainer.innerHTML = faqs.map((faq, index) => `
          <div class="accordion-item ${index === 0 ? 'active' : ''}">
            <div class="accordion-header">
              <span style="display: flex; align-items: center; gap: 0.75rem;">
                <span class="faq-number-badge">Q${index + 1}</span>
                <span style="font-weight: 700; color: #0f172a;">${faq.question}</span>
              </span>
              <span class="accordion-icon">▼</span>
            </div>
            <div class="accordion-content">
              ${faq.answer}
            </div>
          </div>
        `).join('');

        initFAQAccordion();
      }
    } catch (err) {
      console.log('Default FAQs active');
    }
  }

  // Load Published KB Articles from Backend API
  if (kbListContainer) {
    try {
      const res = await API.get('/knowledge-base/public');
      const articles = res.data || [];

      if (articles.length > 0) {
        kbListContainer.innerHTML = articles.slice(0, 4).map(art => `
          <div class="panel-card" style="margin-bottom: 0; padding: 1.25rem; border-top: 3px solid #f59e0b; border-radius: 12px; background: #ffffff; box-shadow: 0 2px 8px rgba(0,0,0,0.03);">
            <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 0.25rem;">
              <h4 style="color: var(--navy-primary); margin: 0; font-size: 1rem; font-weight: 700;">${art.title}</h4>
              ${art.categoryName ? `<span style="font-size: 0.7rem; background: #eff6ff; color: #2563eb; padding: 0.15rem 0.5rem; border-radius: 10px; font-weight: 700;">${art.categoryName}</span>` : ''}
            </div>
            <p style="font-size: 0.875rem; color: #475569; margin: 0.35rem 0 0 0; line-height: 1.5; white-space: pre-line;">${art.content ? (art.content.length > 130 ? art.content.substring(0, 130) + '...' : art.content) : 'Click to view full knowledge article.'}</p>
          </div>
        `).join('');
      }
    } catch (err) {
      console.log('Default KB active');
    }
  }
}
