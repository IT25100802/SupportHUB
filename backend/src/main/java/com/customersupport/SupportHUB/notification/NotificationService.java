package com.customersupport.SupportHUB.notification;

import java.util.List;

public interface NotificationService {
    Notification sendNotification(Notification notification);
    List<NotificationDto> getUserNotifications(String userEmail);
    List<NotificationDto> getUnreadUserNotifications(String userEmail);
    long getUnreadCount(String userEmail);
    NotificationDto markAsRead(Long id, String userEmail);
    void markAllAsRead(String userEmail);
}
