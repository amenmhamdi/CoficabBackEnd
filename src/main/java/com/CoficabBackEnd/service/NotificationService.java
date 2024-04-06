package com.CoficabBackEnd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CoficabBackEnd.controller.NotificationController;
import com.CoficabBackEnd.entity.Notification;
import com.CoficabBackEnd.repository.NotificationRepository;
import com.CoficabBackEnd.repository.UserRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private NotificationController notificationController;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Autowired
    public void setNotificationController(NotificationController notificationController) {
        this.notificationController = notificationController;
    }

    public void markAllNotificationsAsRead(String receiverUsername) {
        List<Notification> notifications = notificationRepository.findByReceiver_UserName(receiverUsername);
        for (Notification notification : notifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }
    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository
    
    public Notification addNotification(Notification notification) {
        // Save the notification to the database
        Notification savedNotification = notificationRepository.save(notification);
        return savedNotification;
    }
    

    public Notification getNotification(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    public List<Notification> getNotificationsBySenderUsername(String senderUsername) {
        return notificationRepository.findBySender_UserName(senderUsername);
    }

    public List<Notification> getNotificationsByReceiverUsername(String receiverUsername) {
        return notificationRepository.findByReceiver_UserName(receiverUsername);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public void deleteAllNotifications() {
        notificationRepository.deleteAll();
    }
}
