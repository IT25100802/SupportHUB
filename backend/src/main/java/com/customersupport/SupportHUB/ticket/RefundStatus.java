package com.customersupport.SupportHUB.ticket;

/**
 * Lifecycle states for an E-Commerce refund request.
 */
public enum RefundStatus {
    NONE,
    REQUESTED,
    PROCESSING,
    APPROVED,
    REJECTED,
    REFUNDED
}
