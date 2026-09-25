package com.customersupport.SupportHUB.notification;

import com.customersupport.SupportHUB.common.User;
import com.customersupport.SupportHUB.ticket.Ticket;

import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

    public Notification createTicketNotification(User user, NotificationType type, Ticket ticket) {
        String title;
        String message;

        switch (type) {
            case TICKET_CREATED:
                title = "New Ticket Created: " + ticket.getTicketNumber();
                message = "Your ticket '" + ticket.getSubject() + "' has been submitted successfully.";
                break;

            case TICKET_ASSIGNED:
                title = "Ticket Assigned: " + ticket.getTicketNumber();
                message = "Ticket '" + ticket.getSubject() + "' has been assigned to support officer.";
                break;

            case TICKET_STATUS_CHANGED:
                title = "Ticket Status Updated: " + ticket.getTicketNumber();
                message = "Ticket '" + ticket.getSubject() + "' status changed to " + ticket.getStatus().name() + ".";
                break;

            case TICKET_REPLIED:
                title = "New Reply on Ticket: " + ticket.getTicketNumber();
                message = "A new reply was posted on ticket '" + ticket.getSubject() + "'.";
                break;

            case TICKET_RESOLVED:
                title = "Ticket Resolved: " + ticket.getTicketNumber();
                message = "Ticket '" + ticket.getSubject() + "' has been marked as RESOLVED. Please provide your feedback.";
                break;

            case TICKET_CLOSED:
                title = "Ticket Closed: " + ticket.getTicketNumber();
                message = "Ticket '" + ticket.getSubject() + "' has been CLOSED.";
                break;

            case FEEDBACK_RECEIVED:
                title = "New Feedback: " + ticket.getTicketNumber();
                message = "Customer submitted feedback for ticket '" + ticket.getSubject() + "'.";
                break;

            case SLA_BREACH:
                title = "⚠️ SLA Breach Alert: " + ticket.getTicketNumber();
                message = "Ticket '" + ticket.getSubject() + "' has exceeded SLA deadline and requires immediate attention.";
                break;

            default:
                title = "System Notification";
                message = "Update regarding ticket " + ticket.getTicketNumber();
                break;
        }

        return new Notification(user, title, message, type, ticket.getId());
    }

    public Notification createCustomNotification(User user, String title, String message, NotificationType type, Long ticketId) {
        return new Notification(user, title, message, type, ticketId);
    }

    public Notification createSystemNotification(User user, String title, String message) {
        return new Notification(user, title, message, NotificationType.SYSTEM, null);
    }
}
