/**
 * SupportHUB Virtual Support Assistant - Production Enterprise Chatbot
 * Fully integrated with SupportHUB Backend, Real Database, FAQs, Knowledge Base & Ticket Creation
 */

const ChatbotAssistant = {
  state: {
    isOpen: false,
    isMinimized: false,
    isLoading: false,
    currentFlow: null, // null | 'TICKET_CREATE' | 'TICKET_TRACK' | 'KB_SEARCH'
    ticketStep: 0,
    ticketDraft: {
      categoryId: null,
      categoryName: '',
      subcategoryId: null,
      subcategoryName: '',
      orderNumber: '',
      trackingNumber: '',
      refundStatus: 'NONE',
      subject: '',
      description: '',
      priority: 'MEDIUM'
    },
    activeCategories: [],
    activeSubcategories: []
  },

  init() {
    const user = (typeof Auth !== 'undefined' && Auth.getUser) ? Auth.getUser() : null;
    if (!user || user.role !== 'CUSTOMER') {
      return;
    }

    this.renderElements();
    this.attachEvents();
  },

  renderElements() {
    if (document.getElementById('floating-chatbot-btn')) return;

    // 1. Floating Trigger Button in Bottom-Right
    const floatBtn = document.createElement('a');
    floatBtn.id = 'floating-chatbot-btn';
    floatBtn.className = 'floating-chatbot-btn';
    floatBtn.href = 'javascript:void(0);';
    floatBtn.title = 'Chat with SupportHUB Assistant';
    floatBtn.innerHTML = `
      <span class="chat-pulse-dot"></span>
      <i class="fa-solid fa-robot"></i>
      <span class="floating-chatbot-tooltip">Need Help? Chat with Assistant 🤖</span>
    `;
    document.body.appendChild(floatBtn);

    // 2. Chatbot Popup Window
    const win = document.createElement('div');
    win.id = 'chatbot-popup-window';
    win.className = 'chatbot-popup-window';
    win.innerHTML = `
      <div class="chatbot-popup-header" id="chatbot-header">
        <div class="chatbot-header-title-row">
          <div class="chatbot-bot-avatar">
            <i class="fa-solid fa-robot"></i>
            <span class="chatbot-online-indicator"></span>
          </div>
          <div class="chatbot-header-info">
            <h4>SupportHUB Assistant</h4>
            <p>Here to help with your orders and support requests.</p>
          </div>
        </div>
        <div class="chatbot-header-actions">
          <button class="chatbot-header-btn" id="chatbot-min-btn" title="Minimize/Expand">
            <i class="fa-solid fa-minus"></i>
          </button>
          <button class="chatbot-header-btn" id="chatbot-close-btn" title="Close Assistant">
            <i class="fa-solid fa-xmark"></i>
          </button>
        </div>
      </div>

      <div class="chatbot-popup-body" id="chatbot-body"></div>

      <div class="chatbot-popup-footer" id="chatbot-footer">
        <input type="text" id="chatbot-input" class="chatbot-input-field" placeholder="Type your message..." autocomplete="off" />
        <button id="chatbot-send-btn" class="chatbot-send-btn" title="Send message">
          <i class="fa-solid fa-paper-plane"></i>
        </button>
      </div>
    `;
    document.body.appendChild(win);

    // Render Initial Welcome Message
    this.renderWelcomeMessage();
  },

  attachEvents() {
    const floatBtn = document.getElementById('floating-chatbot-btn');
    const win = document.getElementById('chatbot-popup-window');
    const closeBtn = document.getElementById('chatbot-close-btn');
    const minBtn = document.getElementById('chatbot-min-btn');
    const sendBtn = document.getElementById('chatbot-send-btn');
    const input = document.getElementById('chatbot-input');

    if (floatBtn && win) {
      floatBtn.addEventListener('click', (e) => {
        e.preventDefault();
        this.toggleWindow();
      });
    }

    if (closeBtn) {
      closeBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        this.closeWindow();
      });
    }

    if (minBtn) {
      minBtn.addEventListener('click', (e) => {
        e.stopPropagation();
        this.toggleMinimize();
      });
    }

    if (sendBtn && input) {
      sendBtn.addEventListener('click', () => this.handleSend());
      input.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
          e.preventDefault();
          this.handleSend();
        }
      });
    }
  },

  toggleWindow() {
    const win = document.getElementById('chatbot-popup-window');
    if (!win) return;
    this.state.isOpen = !this.state.isOpen;
    if (this.state.isOpen) {
      win.classList.add('show');
      win.classList.remove('minimized');
      this.state.isMinimized = false;
      const input = document.getElementById('chatbot-input');
      if (input) setTimeout(() => input.focus(), 150);
      this.scrollToBottom();
    } else {
      win.classList.remove('show');
    }
  },

  closeWindow() {
    const win = document.getElementById('chatbot-popup-window');
    if (win) {
      win.classList.remove('show');
      this.state.isOpen = false;
    }
  },

  toggleMinimize() {
    const win = document.getElementById('chatbot-popup-window');
    const minBtn = document.getElementById('chatbot-min-btn');
    if (!win) return;
    this.state.isMinimized = !this.state.isMinimized;
    if (this.state.isMinimized) {
      win.classList.add('minimized');
      if (minBtn) minBtn.innerHTML = '<i class="fa-solid fa-up-right-and-down-left-from-center"></i>';
    } else {
      win.classList.remove('minimized');
      if (minBtn) minBtn.innerHTML = '<i class="fa-solid fa-minus"></i>';
      this.scrollToBottom();
    }
  },

  renderWelcomeMessage() {
    const body = document.getElementById('chatbot-body');
    if (!body) return;

    const user = (typeof Auth !== 'undefined' && Auth.getUser) ? Auth.getUser() : {};
    const fullName = user.fullName || user.email || 'Customer';
    const firstName = fullName.split(' ')[0] || 'Customer';

    body.innerHTML = `
      <div class="chat-bubble-bot">
        <div style="font-weight: 700; font-size: 0.95rem; margin-bottom: 0.35rem; color: #0f172a;">
          Hi ${this.escapeHtml(firstName)} 👋
        </div>
        <div>How can I help you today? You can choose a quick action below or type any question regarding your orders, deliveries, or payments.</div>
        
        <div class="chatbot-chip-container" style="margin-top: 0.75rem;">
          <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketCreation()">
            <i class="fa-solid fa-plus"></i> Create Support Ticket
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.trackTickets()">
            <i class="fa-solid fa-box"></i> Track My Ticket
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.handleIntent('DELIVERY')">
            <i class="fa-solid fa-truck-fast"></i> Delivery Issue
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.handleIntent('REFUND')">
            <i class="fa-solid fa-hand-holding-dollar"></i> Payment / Refund
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.handleIntent('ORDER')">
            <i class="fa-solid fa-cart-shopping"></i> Order Issue
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.handleIntent('ACCOUNT')">
            <i class="fa-solid fa-user-gear"></i> Account Help
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.showKnowledgeBaseGuides()">
            <i class="fa-solid fa-book-open"></i> Search Help Articles
          </button>
        </div>
      </div>
    `;
    this.scrollToBottom();
  },

  async handleSend() {
    const input = document.getElementById('chatbot-input');
    if (!input || this.state.isLoading) return;

    const text = input.value.trim();
    if (!text) return;

    input.value = '';
    this.appendUserMessage(text);

    // If currently in a guided ticket creation flow
    if (this.state.currentFlow === 'TICKET_CREATE') {
      this.handleTicketCreationStep(text);
      return;
    }

    // Default: Intent & FAQ/KB Search via Backend
    await this.processAssistantQuery(text);
  },

  appendUserMessage(text) {
    const body = document.getElementById('chatbot-body');
    if (!body) return;
    const msgDiv = document.createElement('div');
    msgDiv.className = 'chat-bubble-user';
    msgDiv.textContent = text;
    body.appendChild(msgDiv);
    this.scrollToBottom();
  },

  appendBotMessage(html) {
    const body = document.getElementById('chatbot-body');
    if (!body) return;
    const msgDiv = document.createElement('div');
    msgDiv.className = 'chat-bubble-bot';
    msgDiv.innerHTML = html;
    body.appendChild(msgDiv);
    this.scrollToBottom();
  },

  showTyping() {
    const body = document.getElementById('chatbot-body');
    if (!body) return;
    const typingDiv = document.createElement('div');
    typingDiv.id = 'chatbot-typing-indicator';
    typingDiv.className = 'chatbot-typing-bubble';
    typingDiv.innerHTML = `
      <span class="chatbot-typing-dot"></span>
      <span class="chatbot-typing-dot"></span>
      <span class="chatbot-typing-dot"></span>
      <span style="font-size: 0.75rem; color: #64748b; margin-left: 6px;">SupportHUB Assistant is typing...</span>
    `;
    body.appendChild(typingDiv);
    this.scrollToBottom();
  },

  hideTyping() {
    const typing = document.getElementById('chatbot-typing-indicator');
    if (typing) typing.remove();
  },

  async processAssistantQuery(question) {
    this.state.isLoading = true;
    this.showTyping();

    const lower = question.toLowerCase();

    // Direct Intent Shortcuts
    if (lower.includes('track') && (lower.includes('ticket') || lower.includes('status'))) {
      this.hideTyping();
      this.state.isLoading = false;
      await this.trackTickets();
      return;
    }

    if (lower.includes('create') && (lower.includes('ticket') || lower.includes('issue') || lower.includes('complain'))) {
      this.hideTyping();
      this.state.isLoading = false;
      await this.startTicketCreation();
      return;
    }

    try {
      const res = await API.post('/chatbot/query', { question });
      this.hideTyping();
      this.state.isLoading = false;

      const data = res.data || {};
      let html = `<div style="margin-bottom: 0.5rem;">${this.escapeHtml(data.answer || 'I am looking into that for you.')}</div>`;

      // Related FAQs
      if (data.relatedFaqs && data.relatedFaqs.length > 0) {
        html += `<div style="font-size: 0.78rem; font-weight: 700; color: #475569; margin-top: 0.6rem; margin-bottom: 0.3rem;">Related FAQs:</div>`;
        data.relatedFaqs.slice(0, 3).forEach(faq => {
          html += `
            <div class="chatbot-card-item">
              <div style="font-weight: 700; color: #1e40af; font-size: 0.82rem;">${this.escapeHtml(faq.question)}</div>
              <div style="font-size: 0.78rem; color: #475569; line-height: 1.4;">${this.escapeHtml(faq.answer || '').substring(0, 110)}...</div>
            </div>
          `;
        });
      }

      // Related Knowledge Base Articles
      if (data.relatedKbArticles && data.relatedKbArticles.length > 0) {
        html += `<div style="font-size: 0.78rem; font-weight: 700; color: #475569; margin-top: 0.6rem; margin-bottom: 0.3rem;">Help Center Guides:</div>`;
        data.relatedKbArticles.slice(0, 2).forEach(kb => {
          html += `
            <div class="chatbot-card-item" style="border-left: 3px solid #2563eb;">
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <span style="font-weight: 700; color: #0f172a; font-size: 0.82rem;">${this.escapeHtml(kb.title)}</span>
                <span style="font-size: 0.7rem; color: #64748b;">${this.escapeHtml(kb.categoryName || 'Guide')}</span>
              </div>
              <div style="font-size: 0.76rem; color: #64748b;">${this.escapeHtml(kb.content || '').substring(0, 90)}...</div>
              <div style="text-align: right; margin-top: 0.2rem;">
                <a href="knowledge-base.html" style="font-size: 0.75rem; font-weight: 700; color: #2563eb; text-decoration: none;">Read Guide →</a>
              </div>
            </div>
          `;
        });
      }

      // If suggest ticket is true
      html += `
        <div class="chatbot-chip-container" style="margin-top: 0.75rem;">
          <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketCreation()">
            <i class="fa-solid fa-plus"></i> Create Support Ticket
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.showKnowledgeBaseGuides()">
            <i class="fa-solid fa-book-open"></i> Search Help Center
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
            <i class="fa-solid fa-house"></i> Main Menu
          </button>
        </div>
      `;

      this.appendBotMessage(html);

    } catch (err) {
      this.hideTyping();
      this.state.isLoading = false;
      this.appendBotMessage(`
        <div>I'm having trouble retrieving that information right now. If you need urgent assistance, you can create a support ticket directly:</div>
        <div class="chatbot-chip-container" style="margin-top: 0.6rem;">
          <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketCreation()">
            <i class="fa-solid fa-plus"></i> Create Support Ticket
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
            <i class="fa-solid fa-house"></i> Main Menu
          </button>
        </div>
      `);
    }
  },

  async trackTickets() {
    this.showTyping();
    try {
      const res = await API.get('/tickets/my?size=5');
      this.hideTyping();
      const pageData = res.data || {};
      const tickets = pageData.content || pageData || [];

      if (!Array.isArray(tickets) || tickets.length === 0) {
        this.appendBotMessage(`
          <div style="font-weight: 700; margin-bottom: 0.35rem; color: #0f172a;">Your Support Tickets</div>
          <div>You haven't submitted any support tickets yet. If you are experiencing an order or account issue, I can assist you in creating one now.</div>
          <div class="chatbot-chip-container" style="margin-top: 0.75rem;">
            <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketCreation()">
              <i class="fa-solid fa-plus"></i> Create Your First Ticket
            </button>
            <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
              <i class="fa-solid fa-house"></i> Main Menu
            </button>
          </div>
        `);
        return;
      }

      let html = `<div style="font-weight: 700; margin-bottom: 0.4rem; color: #0f172a;">Your Recent Support Tickets:</div>`;

      tickets.slice(0, 4).forEach(t => {
        const statusBadge = (typeof Utils !== 'undefined' && Utils.getStatusBadge) ? Utils.getStatusBadge(t.status) : `<span class="badge">${t.status}</span>`;
        const priorityBadge = (typeof Utils !== 'undefined' && Utils.getPriorityBadge) ? Utils.getPriorityBadge(t.priority) : `<span class="badge">${t.priority}</span>`;
        const lastUpdated = (typeof Utils !== 'undefined' && Utils.formatDate) ? Utils.formatDate(t.updatedAt || t.createdAt) : 'Recently';

        html += `
          <div class="chatbot-card-item">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span style="font-family: monospace; font-weight: 800; color: #2563eb; font-size: 0.85rem;">${t.ticketNumber}</span>
              <div style="display: flex; gap: 0.3rem;">
                ${priorityBadge}
                ${statusBadge}
              </div>
            </div>
            <div style="font-weight: 700; font-size: 0.82rem; color: #0f172a; margin-top: 0.2rem;">${this.escapeHtml(t.subject)}</div>
            ${t.orderNumber ? `<div style="font-size: 0.75rem; color: #0284c7; font-weight: 600;"><i class="fa-solid fa-box"></i> Order: ${t.orderNumber}</div>` : ''}
            <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 0.35rem; font-size: 0.72rem; color: #64748b;">
              <span>Updated: ${lastUpdated}</span>
              <a href="ticket-details.html?id=${t.id}" class="btn btn-secondary btn-sm" style="padding: 0.2rem 0.55rem; font-size: 0.72rem; text-decoration: none; font-weight: 700;">
                <i class="fa-solid fa-eye"></i> View Ticket
              </a>
            </div>
          </div>
        `;
      });

      html += `
        <div class="chatbot-chip-container" style="margin-top: 0.75rem;">
          <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketCreation()">
            <i class="fa-solid fa-plus"></i> Create Another Ticket
          </button>
          <a href="my-tickets.html" class="chatbot-chip-btn">
            <i class="fa-solid fa-list-check"></i> View All in My Tickets
          </a>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
            <i class="fa-solid fa-house"></i> Main Menu
          </button>
        </div>
      `;

      this.appendBotMessage(html);

    } catch (err) {
      this.hideTyping();
      this.appendBotMessage(`
        <div>Unable to fetch your tickets right now. Please verify your connection or visit the <a href="my-tickets.html" style="color: #2563eb; font-weight: 700;">My Tickets</a> page.</div>
      `);
    }
  },

  handleIntent(intentKey) {
    if (intentKey === 'DELIVERY') {
      this.appendBotMessage(`
        <div style="font-weight: 700; font-size: 0.9rem; margin-bottom: 0.3rem; color: #0f172a;">🚚 Delivery & Shipment Support</div>
        <div>Common delivery solutions:</div>
        <ul style="margin: 0.4rem 0; padding-left: 1.2rem; font-size: 0.8rem; color: #334155; line-height: 1.5;">
          <li>Courier deliveries usually take 2-4 business days.</li>
          <li>Use your Tracking ID with the assigned courier partner.</li>
          <li>If delivery is delayed past 5 days, our operations team will expedite it.</li>
        </ul>
        <div class="chatbot-chip-container">
          <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketWithCategory('Delivery Issues')">
            <i class="fa-solid fa-plus"></i> Open Delivery Ticket
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.askQuestion('How can I track my order?')">
            How to Track Order?
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
            <i class="fa-solid fa-house"></i> Main Menu
          </button>
        </div>
      `);
    } else if (intentKey === 'REFUND') {
      this.appendBotMessage(`
        <div style="font-weight: 700; font-size: 0.9rem; margin-bottom: 0.3rem; color: #0f172a;">💳 Payment & Refund Support</div>
        <div>Standard refund guidelines:</div>
        <ul style="margin: 0.4rem 0; padding-left: 1.2rem; font-size: 0.8rem; color: #334155; line-height: 1.5;">
          <li>Card & Bank refunds take 3-5 business days upon approval.</li>
          <li>Duplicate charges are reversed automatically within 48 hours.</li>
          <li>We support Visa, Mastercard, AMEX, Koko, and Cash on Delivery.</li>
        </ul>
        <div class="chatbot-chip-container">
          <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketWithCategory('Payment & Refunds')">
            <i class="fa-solid fa-plus"></i> Request a Refund Ticket
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.askQuestion('How do I request a refund?')">
            Refund Steps
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
            <i class="fa-solid fa-house"></i> Main Menu
          </button>
        </div>
      `);
    } else if (intentKey === 'ORDER') {
      this.appendBotMessage(`
        <div style="font-weight: 700; font-size: 0.9rem; margin-bottom: 0.3rem; color: #0f172a;">🛒 Order & Return Support</div>
        <div>You can request a replacement or return within 14 days of delivery for damaged or incorrect items.</div>
        <div class="chatbot-chip-container" style="margin-top: 0.6rem;">
          <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketWithCategory('Order Issues')">
            <i class="fa-solid fa-plus"></i> Report Order Issue
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.askQuestion('What to do if you receive a damaged item?')">
            Damaged Item Guide
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
            <i class="fa-solid fa-house"></i> Main Menu
          </button>
        </div>
      `);
    } else if (intentKey === 'ACCOUNT') {
      this.appendBotMessage(`
        <div style="font-weight: 700; font-size: 0.9rem; margin-bottom: 0.3rem; color: #0f172a;">👤 Account & Security Support</div>
        <div>You can update your personal contact info, change passwords, and view security status under <a href="customer-profile.html" style="color: #2563eb; font-weight: 700;">My Profile</a>.</div>
        <div class="chatbot-chip-container" style="margin-top: 0.6rem;">
          <a href="customer-profile.html" class="chatbot-chip-btn chatbot-chip-primary">
            <i class="fa-solid fa-user-pen"></i> Go to My Profile
          </a>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.startTicketWithCategory('Account Issues')">
            <i class="fa-solid fa-plus"></i> Open Account Ticket
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
            <i class="fa-solid fa-house"></i> Main Menu
          </button>
        </div>
      `);
    }
  },

  async showKnowledgeBaseGuides() {
    this.showTyping();
    try {
      const res = await API.get('/knowledge-base/public');
      this.hideTyping();
      const articles = res.data || [];

      if (!articles || articles.length === 0) {
        this.appendBotMessage(`
          <div>Browse our full Help Center on the <a href="knowledge-base.html" style="color: #2563eb; font-weight: 700;">Knowledge Base</a> page.</div>
        `);
        return;
      }

      let html = `<div style="font-weight: 700; margin-bottom: 0.4rem; color: #0f172a;">Popular Help Center Guides:</div>`;
      articles.slice(0, 4).forEach(a => {
        html += `
          <div class="chatbot-card-item">
            <div style="font-weight: 700; color: #0f172a; font-size: 0.82rem;">${this.escapeHtml(a.title)}</div>
            <div style="font-size: 0.76rem; color: #64748b;">${this.escapeHtml(a.content || '').substring(0, 90)}...</div>
            <div style="text-align: right; margin-top: 0.25rem;">
              <a href="knowledge-base.html" style="font-size: 0.75rem; font-weight: 700; color: #2563eb; text-decoration: none;">Read Guide →</a>
            </div>
          </div>
        `;
      });

      html += `
        <div class="chatbot-chip-container" style="margin-top: 0.75rem;">
          <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketCreation()">
            <i class="fa-solid fa-plus"></i> Create Support Ticket
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
            <i class="fa-solid fa-house"></i> Main Menu
          </button>
        </div>
      `;

      this.appendBotMessage(html);

    } catch (err) {
      this.hideTyping();
      this.appendBotMessage(`
        <div>You can explore all guides on the <a href="knowledge-base.html" style="color: #2563eb; font-weight: 700;">Knowledge Base</a> page.</div>
      `);
    }
  },

  askQuestion(q) {
    const input = document.getElementById('chatbot-input');
    if (input) {
      input.value = q;
      this.handleSend();
    }
  },

  // =========================================================================
  // GUIDED TICKET CREATION CONVERSATIONAL FLOW
  // =========================================================================

  disablePreviousInteractiveButtons() {
    const body = document.getElementById('chatbot-body');
    if (!body) return;
    const containers = body.querySelectorAll('.chatbot-chip-container');
    containers.forEach(container => {
      const buttons = container.querySelectorAll('button');
      buttons.forEach(btn => {
        btn.disabled = true;
        btn.style.pointerEvents = 'none';
        btn.style.opacity = '0.55';
        btn.style.cursor = 'default';
      });
    });
  },

  async startTicketCreation() {
    this.disablePreviousInteractiveButtons();
    this.state.currentFlow = 'TICKET_CREATE';
    this.state.ticketStep = 1;
    this.state.ticketDraft = {
      categoryId: null,
      categoryName: '',
      subcategoryId: null,
      subcategoryName: '',
      orderNumber: '',
      trackingNumber: '',
      refundStatus: 'NONE',
      subject: '',
      description: '',
      priority: 'MEDIUM'
    };

    this.showTyping();
    try {
      const res = await API.get('/categories/active');
      this.hideTyping();
      this.state.activeCategories = res.data || [];

      if (this.state.activeCategories.length === 0) {
        this.appendBotMessage(`
          <div>No support categories found. Please try again later or contact support.</div>
        `);
        this.state.currentFlow = null;
        return;
      }

      let chipBtns = this.state.activeCategories.map(c => `
        <button class="chatbot-chip-btn" onclick="ChatbotAssistant.selectTicketCategory(${c.id}, '${this.escapeHtml(c.name)}')">
          ${this.getCategoryIcon(c.name)} ${this.escapeHtml(c.name)}
        </button>
      `).join('');

      this.appendBotMessage(`
        <div style="font-weight: 700; font-size: 0.9rem; color: #0f172a; margin-bottom: 0.35rem;">
          🎫 Step 1 of 5: Select Issue Category
        </div>
        <div>What main category best describes the issue you need help with?</div>
        <div class="chatbot-chip-container" style="margin-top: 0.6rem;">
          ${chipBtns}
          <button class="chatbot-chip-btn" style="color: #dc2626; border-color: #fecaca;" onclick="ChatbotAssistant.cancelTicketCreation()">
            <i class="fa-solid fa-xmark"></i> Cancel
          </button>
        </div>
      `);

    } catch (err) {
      this.hideTyping();
      this.appendBotMessage(`
        <div>Failed to load categories. Please try again later.</div>
      `);
      this.state.currentFlow = null;
    }
  },

  async startTicketWithCategory(catName) {
    await this.startTicketCreation();
    const found = (this.state.activeCategories || []).find(c => c.name.toLowerCase().includes(catName.toLowerCase()));
    if (found) {
      await this.selectTicketCategory(found.id, found.name);
    }
  },

  async selectTicketCategory(catId, catName) {
    if (this.state.currentFlow !== 'TICKET_CREATE' || this.state.ticketStep !== 1) {
      this.appendBotMessage(`<div>The previous ticket flow was reset. Let's start fresh from Step 1:</div>`);
      await this.startTicketCreation();
      return;
    }

    this.disablePreviousInteractiveButtons();
    this.state.ticketDraft.categoryId = catId;
    this.state.ticketDraft.categoryName = catName;
    this.state.ticketStep = 2;

    this.appendUserMessage(catName);
    this.showTyping();

    try {
      const res = await API.get(`/categories/${catId}/subcategories`);
      this.hideTyping();
      this.state.activeSubcategories = res.data || [];

      if (this.state.activeSubcategories && this.state.activeSubcategories.length > 0) {
        let subChips = this.state.activeSubcategories.map(s => `
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.selectTicketSubcategory(${s.id}, '${this.escapeHtml(s.name)}')">
            • ${this.escapeHtml(s.name)}
          </button>
        `).join('');

        this.appendBotMessage(`
          <div style="font-weight: 700; font-size: 0.9rem; color: #0f172a; margin-bottom: 0.35rem;">
            🎫 Step 2 of 5: Select Specific Issue
          </div>
          <div>Which specific topic describes your issue under <strong>${this.escapeHtml(catName)}</strong>?</div>
          <div class="chatbot-chip-container" style="margin-top: 0.6rem;">
            ${subChips}
            <button class="chatbot-chip-btn" onclick="ChatbotAssistant.selectTicketSubcategory(null, 'General')">
              • General ${this.escapeHtml(catName)}
            </button>
            <button class="chatbot-chip-btn" style="color: #dc2626; border-color: #fecaca;" onclick="ChatbotAssistant.cancelTicketCreation()">
              <i class="fa-solid fa-xmark"></i> Cancel
            </button>
          </div>
        `);
      } else {
        // No subcategories, proceed to Order # / Subject
        this.askOrderOrSubject();
      }
    } catch (err) {
      this.hideTyping();
      this.askOrderOrSubject();
    }
  },

  selectTicketSubcategory(subId, subName) {
    if (this.state.currentFlow !== 'TICKET_CREATE' || this.state.ticketStep !== 2) {
      this.appendBotMessage(`<div>The previous ticket session was reset. Let's start from Step 1:</div>`);
      this.startTicketCreation();
      return;
    }

    this.disablePreviousInteractiveButtons();
    this.state.ticketDraft.subcategoryId = subId;
    this.state.ticketDraft.subcategoryName = subName || '';
    this.appendUserMessage(subName);
    this.askOrderOrSubject();
  },

  askOrderOrSubject() {
    this.state.ticketStep = 3;
    const cat = (this.state.ticketDraft.categoryName || '').toLowerCase();
    const isOrderRelated = cat.includes('order') || cat.includes('deliver') || cat.includes('payment') || cat.includes('refund');

    if (isOrderRelated) {
      this.appendBotMessage(`
        <div style="font-weight: 700; font-size: 0.9rem; color: #0f172a; margin-bottom: 0.35rem;">
          📦 Step 3 of 5: Related Order (Optional)
        </div>
        <div>Do you have a related Order # (e.g. <strong>ORD-50218</strong>)? If yes, please type your Order number below, or click Skip:</div>
        <div class="chatbot-chip-container" style="margin-top: 0.6rem;">
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.skipOrderNumber()">
            <i class="fa-solid fa-forward"></i> Skip Order #
          </button>
          <button class="chatbot-chip-btn" style="color: #dc2626; border-color: #fecaca;" onclick="ChatbotAssistant.cancelTicketCreation()">
            <i class="fa-solid fa-xmark"></i> Cancel
          </button>
        </div>
      `);
    } else {
      this.askTicketSubject();
    }
  },

  skipOrderNumber() {
    if (this.state.currentFlow !== 'TICKET_CREATE' || this.state.ticketStep !== 3) {
      this.appendBotMessage(`<div>The previous ticket session was cancelled. Let's start fresh from Step 1:</div>`);
      this.startTicketCreation();
      return;
    }

    this.disablePreviousInteractiveButtons();
    this.state.ticketDraft.orderNumber = '';
    this.appendUserMessage('Skip Order #');
    this.askTicketSubject();
  },

  askTicketSubject() {
    this.state.ticketStep = 4;
    this.appendBotMessage(`
      <div style="font-weight: 700; font-size: 0.9rem; color: #0f172a; margin-bottom: 0.35rem;">
        📝 Step 4 of 5: Ticket Subject
      </div>
      <div>Please type a brief subject / summary for your ticket (e.g. <em>"Refund not received for returned item"</em>):</div>
    `);
  },

  askTicketDescription() {
    this.state.ticketStep = 5;
    this.appendBotMessage(`
      <div style="font-weight: 700; font-size: 0.9rem; color: #0f172a; margin-bottom: 0.35rem;">
        📄 Step 5 of 5: Problem Description
      </div>
      <div>Please describe the issue in detail so our support team can assist you quickly:</div>
    `);
  },

  askTicketPriority() {
    this.state.ticketStep = 6;
    this.appendBotMessage(`
      <div style="font-weight: 700; font-size: 0.9rem; color: #0f172a; margin-bottom: 0.35rem;">
        ⚡ Select Priority Level
      </div>
      <div>How urgent is this issue?</div>
      <div class="chatbot-chip-container" style="margin-top: 0.6rem;">
        <button class="chatbot-chip-btn" onclick="ChatbotAssistant.setTicketPriority('LOW')">
          🟢 Low - General Inquiry
        </button>
        <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.setTicketPriority('MEDIUM')">
          🟡 Medium - Standard Issue
        </button>
        <button class="chatbot-chip-btn" onclick="ChatbotAssistant.setTicketPriority('HIGH')">
          🔴 High - Urgent Attention
        </button>
        <button class="chatbot-chip-btn" style="color: #dc2626; border-color: #fecaca;" onclick="ChatbotAssistant.cancelTicketCreation()">
          <i class="fa-solid fa-xmark"></i> Cancel
        </button>
      </div>
    `);
  },

  setTicketPriority(p) {
    if (this.state.currentFlow !== 'TICKET_CREATE' || this.state.ticketStep !== 6) {
      this.appendBotMessage(`<div>The previous ticket session was reset. Let's start fresh from Step 1:</div>`);
      this.startTicketCreation();
      return;
    }

    this.disablePreviousInteractiveButtons();
    this.state.ticketDraft.priority = p;
    this.appendUserMessage(`Priority: ${p}`);
    this.showConfirmationSummary();
  },

  handleTicketCreationStep(text) {
    if (this.state.currentFlow !== 'TICKET_CREATE') {
      return;
    }

    if (this.state.ticketStep === 3) {
      this.disablePreviousInteractiveButtons();
      this.state.ticketDraft.orderNumber = text;
      this.askTicketSubject();
    } else if (this.state.ticketStep === 4) {
      this.state.ticketDraft.subject = text;
      this.askTicketDescription();
    } else if (this.state.ticketStep === 5) {
      this.state.ticketDraft.description = text;
      this.askTicketPriority();
    }
  },

  showConfirmationSummary() {
    this.state.ticketStep = 7;
    const d = this.state.ticketDraft;

    this.appendBotMessage(`
      <div style="font-weight: 800; font-size: 0.95rem; color: #0f172a; margin-bottom: 0.5rem;">
        🔍 Review Your Support Ticket
      </div>
      <div style="background: #ffffff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 0.85rem; font-size: 0.8rem; line-height: 1.6;">
        <div><strong>Category:</strong> ${this.escapeHtml(d.categoryName)} ${d.subcategoryName ? `→ ${this.escapeHtml(d.subcategoryName)}` : ''}</div>
        <div><strong>Related Order:</strong> ${d.orderNumber ? `<span style="font-family: monospace; color: #0284c7; font-weight: 700;">${this.escapeHtml(d.orderNumber)}</span>` : '<span style="color: #94a3b8;">None</span>'}</div>
        <div><strong>Priority:</strong> <span style="font-weight: 700; color: #d97706;">${d.priority}</span></div>
        <div style="margin-top: 0.35rem;"><strong>Subject:</strong> ${this.escapeHtml(d.subject)}</div>
        <div style="margin-top: 0.35rem; color: #475569; font-style: italic; border-top: 1px dashed #e2e8f0; padding-top: 0.35rem;">"${this.escapeHtml(d.description)}"</div>
      </div>
      <div class="chatbot-chip-container" style="margin-top: 0.75rem;">
        <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.submitTicketToBackend()">
          <i class="fa-solid fa-paper-plane"></i> Submit Ticket Now
        </button>
        <button class="chatbot-chip-btn" style="color: #dc2626; border-color: #fecaca;" onclick="ChatbotAssistant.cancelTicketCreation()">
          <i class="fa-solid fa-xmark"></i> Cancel
        </button>
      </div>
    `);
  },

  async submitTicketToBackend() {
    if (this.state.currentFlow !== 'TICKET_CREATE' || this.state.ticketStep !== 7 || !this.state.ticketDraft.categoryId) {
      this.appendBotMessage(`<div>This ticket session is no longer active. Let's create a new ticket from the beginning:</div>`);
      await this.startTicketCreation();
      return;
    }

    this.disablePreviousInteractiveButtons();
    this.showTyping();
    const d = this.state.ticketDraft;

    const payload = {
      categoryId: d.categoryId,
      subcategoryId: d.subcategoryId || null,
      subject: d.subject,
      description: d.description,
      priority: d.priority || 'MEDIUM',
      orderNumber: d.orderNumber || null,
      trackingNumber: d.trackingNumber || null,
      refundStatus: d.refundStatus || 'NONE'
    };

    try {
      const res = await API.post('/tickets/create-json', payload);
      this.hideTyping();

      const created = res.data || {};
      const tktNumber = created.ticketNumber || 'TKT-NEW';
      const tktId = created.id || '';

      this.state.currentFlow = null;
      this.state.ticketStep = 0;
      this.state.ticketDraft = {};

      this.appendBotMessage(`
        <div style="text-align: center; padding: 0.5rem 0;">
          <div style="font-size: 2rem; margin-bottom: 0.3rem;">🎉</div>
          <div style="font-weight: 800; font-size: 1.05rem; color: #059669;">Ticket Created Successfully!</div>
          <div style="font-size: 0.82rem; color: #475569; margin-top: 0.2rem;">Your support ticket is now active in our system:</div>
          <div style="font-size: 1.15rem; font-family: monospace; font-weight: 800; color: #2563eb; margin: 0.5rem 0;">${tktNumber}</div>
          <div style="font-size: 0.78rem; color: #64748b;">A Support Officer will review your request shortly.</div>
        </div>
        <div class="chatbot-chip-container" style="margin-top: 0.75rem; justify-content: center;">
          ${tktId ? `<a href="ticket-details.html?id=${tktId}" class="chatbot-chip-btn chatbot-chip-primary"><i class="fa-solid fa-eye"></i> View Ticket Details</a>` : ''}
          <a href="my-tickets.html" class="chatbot-chip-btn"><i class="fa-solid fa-list-check"></i> My Tickets</a>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()"><i class="fa-solid fa-house"></i> Main Menu</button>
        </div>
      `);

      if (typeof Utils !== 'undefined' && Utils.showToast) {
        Utils.showToast(`Support Ticket ${tktNumber} created successfully!`, 'success');
      }

    } catch (err) {
      this.hideTyping();
      this.appendBotMessage(`
        <div style="color: #dc2626; font-weight: 700; margin-bottom: 0.3rem;">
          <i class="fa-solid fa-triangle-exclamation"></i> Submission Error
        </div>
        <div>Something went wrong while submitting your ticket (${this.escapeHtml(err.message || 'Server error')}). Your ticket was not submitted.</div>
        <div class="chatbot-chip-container" style="margin-top: 0.6rem;">
          <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.submitTicketToBackend()">
            <i class="fa-solid fa-rotate-right"></i> Try Again
          </button>
          <button class="chatbot-chip-btn" onclick="ChatbotAssistant.cancelTicketCreation()">
            <i class="fa-solid fa-xmark"></i> Cancel
          </button>
        </div>
      `);
    }
  },

  cancelTicketCreation() {
    this.disablePreviousInteractiveButtons();
    this.state.currentFlow = null;
    this.state.ticketStep = 0;
    this.state.ticketDraft = {
      categoryId: null,
      categoryName: '',
      subcategoryId: null,
      subcategoryName: '',
      orderNumber: '',
      trackingNumber: '',
      refundStatus: 'NONE',
      subject: '',
      description: '',
      priority: 'MEDIUM'
    };

    this.appendBotMessage(`
      <div>Ticket creation cancelled. What else can I help you with?</div>
      <div class="chatbot-chip-container" style="margin-top: 0.6rem;">
        <button class="chatbot-chip-btn chatbot-chip-primary" onclick="ChatbotAssistant.startTicketCreation()">
          <i class="fa-solid fa-plus"></i> Create Support Ticket
        </button>
        <button class="chatbot-chip-btn" onclick="ChatbotAssistant.trackTickets()">
          <i class="fa-solid fa-box"></i> Track My Ticket
        </button>
        <button class="chatbot-chip-btn" onclick="ChatbotAssistant.showKnowledgeBaseGuides()">
          <i class="fa-solid fa-book-open"></i> Help Guides
        </button>
        <button class="chatbot-chip-btn" onclick="ChatbotAssistant.renderWelcomeMessage()">
          <i class="fa-solid fa-house"></i> Main Menu
        </button>
      </div>
    `);
  },

  getCategoryIcon(catName) {
    const c = (catName || '').toLowerCase();
    if (c.includes('deliver') || c.includes('ship')) return '<i class="fa-solid fa-truck-fast" style="color: #3b82f6;"></i>';
    if (c.includes('pay') || c.includes('refund') || c.includes('billing')) return '<i class="fa-solid fa-credit-card" style="color: #ec4899;"></i>';
    if (c.includes('order')) return '<i class="fa-solid fa-cart-shopping" style="color: #f59e0b;"></i>';
    if (c.includes('account') || c.includes('login')) return '<i class="fa-solid fa-user-gear" style="color: #8b5cf6;"></i>';
    if (c.includes('product') || c.includes('warranty')) return '<i class="fa-solid fa-box-open" style="color: #10b981;"></i>';
    return '<i class="fa-solid fa-layer-group" style="color: #64748b;"></i>';
  },

  scrollToBottom() {
    const body = document.getElementById('chatbot-body');
    if (body) {
      setTimeout(() => {
        body.scrollTop = body.scrollHeight;
      }, 50);
    }
  },

  escapeHtml(str) {
    if (!str) return '';
    return String(str)
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;');
  }
};

// Auto-initialize when DOM loads if customer role
document.addEventListener('DOMContentLoaded', () => {
  if (typeof Auth !== 'undefined') {
    const user = Auth.getUser();
    if (user && user.role === 'CUSTOMER') {
      ChatbotAssistant.init();
    }
  }
});
