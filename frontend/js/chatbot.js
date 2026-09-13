/**
 * SupportHUB Enterprise Platform - Interactive Chatbot Controller
 */
const ChatbotWidget = {
  init() {
    this.renderWidget();
    this.attachEvents();
  },

  renderWidget() {
    if (document.getElementById('chatbot-widget-btn')) return;

    const widgetHTML = `
      <div id="chatbot-widget-btn" class="chatbot-widget-btn">🤖</div>
      <div id="chatbot-window" class="chatbot-window">
        <div class="chatbot-header">
          <div style="font-weight: 600; display: flex; align-items: center; gap: 0.5rem;">
            <span>🤖</span> Support FAQ Assistant
          </div>
          <button id="close-chatbot" style="background:none;border:none;color:var(--text-muted);font-size:1.2rem;cursor:pointer;">&times;</button>
        </div>
        <div id="chatbot-messages" class="chatbot-messages">
          <div class="chat-msg bot">
            Hello! I am your FAQ Assistant. Ask me any question regarding accounts, billing, SLAs, or technical support!
          </div>
        </div>
        <div class="chatbot-input-bar">
          <input type="text" id="chatbot-input" class="form-control" placeholder="Ask a question..." />
          <button id="chatbot-send-btn" class="btn btn-primary btn-sm">Send</button>
        </div>
      </div>
    `;

    const container = document.createElement('div');
    container.innerHTML = widgetHTML;
    document.body.appendChild(container);
  },

  attachEvents() {
    const btn = document.getElementById('chatbot-widget-btn');
    const win = document.getElementById('chatbot-window');
    const closeBtn = document.getElementById('close-chatbot');
    const sendBtn = document.getElementById('chatbot-send-btn');
    const input = document.getElementById('chatbot-input');

    if (btn && win) {
      btn.addEventListener('click', () => win.classList.toggle('show'));
    }
    if (closeBtn && win) {
      closeBtn.addEventListener('click', () => win.classList.remove('show'));
    }
    if (sendBtn && input) {
      sendBtn.addEventListener('click', () => this.handleSend());
      input.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') this.handleSend();
      });
    }
  },

  async handleSend() {
    const input = document.getElementById('chatbot-input');
    const messages = document.getElementById('chatbot-messages');
    const question = input.value.trim();

    if (!question) return;

    // Append user message
    messages.innerHTML += `<div class="chat-msg user">${question}</div>`;
    input.value = '';
    messages.scrollTop = messages.scrollHeight;

    // Append typing indicator
    const typingId = 'typing-' + Date.now();
    messages.innerHTML += `<div id="${typingId}" class="chat-msg bot"><em>Thinking...</em></div>`;
    messages.scrollTop = messages.scrollHeight;

    try {
      const res = await API.post('/chatbot/query', { question });
      document.getElementById(typingId).remove();

      const data = res.data;
      let botHtml = `<div class="chat-msg bot">${data.answer}</div>`;

      if (data.relatedFaqs && data.relatedFaqs.length > 0) {
        botHtml += `<div style="font-size:0.75rem; color:var(--text-muted); margin-top:0.3rem;">Suggested Related FAQs:</div>`;
        data.relatedFaqs.forEach(faq => {
          botHtml += `<div style="font-size:0.8rem; color:var(--primary); cursor:pointer; text-decoration:underline; margin-top:0.2rem;" onclick="ChatbotWidget.askQuestion('${faq.question.replaceAll("'", "\\'")}')">• ${faq.question}</div>`;
        });
      }

      if (data.suggestTicket) {
        botHtml += `<div style="margin-top:0.5rem;"><a href="/customer/my-tickets.html" class="btn btn-warning btn-sm" style="font-size:0.75rem; background: #f59e0b; border-color: #d97706; color: #0f172a; font-weight: 700;">Submit Support Ticket</a></div>`;
      }

      messages.innerHTML += botHtml;
      messages.scrollTop = messages.scrollHeight;
    } catch (err) {
      document.getElementById(typingId).remove();
      messages.innerHTML += `<div class="chat-msg bot">Sorry, an error occurred while connecting to the assistant.</div>`;
      messages.scrollTop = messages.scrollHeight;
    }
  },

  askQuestion(q) {
    const input = document.getElementById('chatbot-input');
    if (input) {
      input.value = q;
      this.handleSend();
    }
  }
};
