package com.CoficabBackEnd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.CoficabBackEnd.entity.Notification;
import com.CoficabBackEnd.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final Map<String, SseEmitter> emitters = new HashMap<>();

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications() {
        SseEmitter emitter = new SseEmitter();
        emitters.put("unique_id", emitter); // You might want to generate unique ID for each client
        emitter.onCompletion(() -> emitters.remove("unique_id"));
        emitter.onTimeout(() -> emitters.remove("unique_id"));
        return emitter;
    }

    // Method to send notifications to clients
    public void sendNotification(Notification notification) {
        emitters.forEach((id, emitter) -> {
            try {
                emitter.send(notification);
            } catch (Exception e) {
                emitters.remove(id);
            }
        });
    }

    @PostMapping("/add")
    public ResponseEntity<Notification> addNotification(@RequestBody Notification notification) {
        Notification addedNotification = notificationService.addNotification(notification);
        return new ResponseEntity<>(addedNotification, HttpStatus.CREATED);
    }

    @PutMapping("/mark-all-read/{receiverUsername}")
    public ResponseEntity<Void> markAllNotificationsAsRead(@PathVariable String receiverUsername) {
        notificationService.markAllNotificationsAsRead(receiverUsername);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        Notification notification = notificationService.getNotification(id);
        if (notification != null) {
            return new ResponseEntity<>(notification, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/sender/{senderUsername}")
    public List<Notification> getNotificationsBySenderUsername(@PathVariable String senderUsername) {
        return notificationService.getNotificationsBySenderUsername(senderUsername);
    }

    @GetMapping("/receiver/{receiverUsername}")
    public List<Notification> getNotificationsByReceiverUsername(@PathVariable String receiverUsername) {
        return notificationService.getNotificationsByReceiverUsername(receiverUsername);
    }

    @GetMapping("/all")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllNotifications() {
        notificationService.deleteAllNotifications();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
