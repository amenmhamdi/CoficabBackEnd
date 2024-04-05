package com.CoficabBackEnd.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CoficabBackEnd.entity.Announcement;
import com.CoficabBackEnd.entity.Formation;
import com.CoficabBackEnd.entity.Notification;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.repository.AnnouncementRepository;
import com.CoficabBackEnd.repository.FormationRepository;
import com.CoficabBackEnd.repository.UserRepository;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private UserService userService; // Assuming you have a UserService

    @Autowired
    private NotificationService notificationService; // Inject NotificationService

    public Announcement saveAnnouncement(Announcement announcement, User sender) {
        Formation formation = announcement.getFormation();

        if (formation != null) {
            Long formationId = formation.getFid();
            formation = formationRepository.findById(formationId)
                    .orElseThrow(() -> new RuntimeException("Formation not found with id: " + formationId));
            announcement.setFormation(formation);
        }

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        // Notify users associated with the formation about the new announcement
        notifyUsers(savedAnnouncement, sender, "CREATE");

        return savedAnnouncement;
    }

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Announcement getAnnouncementById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + id));
    }

    public Announcement updateAnnouncement(Long id, Announcement updatedAnnouncement, User sender) {
        Announcement announcement = getAnnouncementById(id);
        if (announcement == null) {
            throw new RuntimeException("Announcement not found with id: " + id);
        }

        // Update fields with values from updatedAnnouncement
        announcement.setMessage(updatedAnnouncement.getMessage());
        announcement.setDescription(updatedAnnouncement.getDescription());
        announcement.setTag(updatedAnnouncement.getTag());

        // Update formation if provided in the updatedAnnouncement
        Formation updatedFormation = updatedAnnouncement.getFormation();
        if (updatedFormation != null) {
            Long formationId = updatedFormation.getFid();
            if (formationId != null) {
                Formation formation = formationRepository.findById(formationId)
                        .orElseThrow(() -> new RuntimeException("Formation not found with id: " + formationId));
                announcement.setFormation(formation);
            } else {
                announcement.setFormation(null); // Clear the formation if formationId is not provided
            }
        }

        // Save the updated announcement
        Announcement savedAnnouncement = announcementRepository.save(announcement);

        // Notify users associated with the formation about the updated announcement
        notifyUsers(savedAnnouncement, sender, "UPDATE");

        return savedAnnouncement;
    }

    public void deleteAnnouncement(Long id, User sender) {
        Announcement deletedAnnouncement = getAnnouncementById(id);
        if (deletedAnnouncement != null) {
            announcementRepository.deleteById(id);

            // Notify users associated with the formation about the deleted announcement
            notifyUsers(deletedAnnouncement, sender, "DELETE");
        }
    }

    public List<Announcement> getAllAnnouncementsForCurrentUser(User currentUser) {
        if (currentUser == null || currentUser.getFormations() == null || currentUser.getFormations().isEmpty()) {
            return Collections.emptyList(); // Return an empty list if the user or user's formations are null or empty
        }
    
        List<Formation> formations = new ArrayList<>(currentUser.getFormations());
        List<Announcement> announcements = new ArrayList<>();
    
        for (Formation formation : formations) {
            announcements.addAll(formation.getAnnouncements());
        }
    
        return announcements;
    }

    private void notifyUsers(Announcement announcement, User sender, String actionType) {
        Formation formation = announcement.getFormation();
        if (formation == null) {
            return; // No formation associated, no users to notify
        }

        // Get the users associated with the formation
        Set<User> users = formation.getUsers();

        for (User user : users) {
            // Check if the user is associated with the formation
            if (user.getFormations().contains(formation)) {
                Notification notification = new Notification();
                String message = "";
                switch (actionType) {
                    case "CREATE":
                        message = "A new announcement has been made: " + announcement.getMessage();
                        break;
                    case "UPDATE":
                        message = "An announcement has been updated: " + announcement.getMessage();
                        break;
                    case "DELETE":
                        message = "An announcement has been deleted: " + announcement.getMessage();
                        break;
                }

                notification.setMessage(message);
                notification.setSender(sender);
                notification.setReceiver(user);
                notificationService.addNotification(notification);
            }
        }
    }

}
