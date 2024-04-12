package com.CoficabBackEnd.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CoficabBackEnd.entity.Announcement;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.service.AnnouncementService;
import com.CoficabBackEnd.service.UserService;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private UserService userService; // Inject UserService

    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        List<Announcement> announcements = announcementService.getAllAnnouncements();
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<List<Announcement>> getAllAnnouncementsForCurrentUser(Principal principal) {
        String username = principal.getName(); // Get the username of the logged-in user
        User currentUser = userService.getUser(username); // Get the user object
        List<Announcement> announcements = announcementService.getAllAnnouncementsForCurrentUser(currentUser);
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable Long id) {
        Announcement announcement = announcementService.getAnnouncementById(id);
        return new ResponseEntity<>(announcement, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement,
            Principal principal) {
        String senderUsername = principal.getName(); // Get the username of the logged-in user
        User sender = new User(); // Create a User object for the sender
        sender.setUserName(senderUsername);

        Announcement createdAnnouncement = announcementService.saveAnnouncement(announcement, sender);
        return new ResponseEntity<>(createdAnnouncement, HttpStatus.CREATED);
    }

    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<Announcement>> getAnnouncementsForFormation(@PathVariable Long formationId) {
        List<Announcement> announcements = announcementService.getAnnouncementsForFormation(formationId);
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable("id") Long id,
            @RequestBody Announcement updatedAnnouncement, Principal principal) {
        String senderUsername = principal.getName(); // Get the username of the logged-in user
        User sender = new User(); // Create a User object for the sender
        sender.setUserName(senderUsername);
    
        Announcement result = announcementService.updateAnnouncement(id, updatedAnnouncement, sender);
        return ResponseEntity.ok(result);
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncement(@PathVariable("id") Long id, Principal principal) {
        String senderUsername = principal.getName(); // Get the username of the logged-in user
        User sender = new User(); // Create a User object for the sender
        sender.setUserName(senderUsername);

        announcementService.deleteAnnouncement(id, sender);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
