package com.CoficabBackEnd.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CoficabBackEnd.entity.Formation;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.service.FormationService;

@RestController
@RequestMapping("/formation")
public class FormationController {

    @Autowired
    private FormationService formationService;

    // Add formation
    @PostMapping("/addFormation")
    public ResponseEntity<Formation> addFormation(@RequestBody Formation formation) {
        Formation addedFormation = this.formationService.addFormation(formation);
        return ResponseEntity.ok(addedFormation);
    }

    // Get formation by ID
    @GetMapping("/getFormation/{formationId}")
    public Formation getFormation(@PathVariable("formationId") Long formationId) {
        return this.formationService.getFormation(formationId);
    }

    // Get all formations
    @GetMapping("/getAllFormations")
    public ResponseEntity<?> getAllFormations() {
        return ResponseEntity.ok(this.formationService.getFormations());
    }

    // Update formation
    @PutMapping("/updateFormation/{formationId}")
    public Formation updateFormation(@PathVariable("formationId") Long formationId, @RequestBody Formation formation) {
        formation.setFid(formationId);
        return this.formationService.updateFormation(formation);
    }

    // Delete formation
    @DeleteMapping("/deleteFormation/{formationId}")
    public void deleteFormation(@PathVariable("formationId") Long formationId) {
        this.formationService.deleteFormation(formationId);
    }

    // Get formations by user name
    @GetMapping("/userFormations/{userName}")
    public List<Formation> getUserFormations(@PathVariable("userName") String userName) {
        return this.formationService.findAllByUserName(userName);
    }

    // Assign users to a formation
    @PostMapping("/assignUsers/{formationId}")
    public ResponseEntity<Formation> assignUsersToFormation(@PathVariable Long formationId,
            @RequestBody List<Map<String, String>> users,
            Principal principal) {
        String senderUsername = principal.getName(); // Get the username of the logged-in user
        User sender = new User(); // Create a User object for the sender
        sender.setUserName(senderUsername);

        List<String> usernames = users.stream()
                .map(user -> user.get("value")) // Extract usernames from value property
                .collect(Collectors.toList());

        Formation updatedFormation = formationService.assignUsersToFormation(formationId, usernames, sender);
        return ResponseEntity.ok(updatedFormation);
    }

    // Get formation count
    @GetMapping("/getFormationCount")
    public ResponseEntity<Long> getFormationCount() {
        Long formationCount = this.formationService.getFormationCount();
        return ResponseEntity.ok(formationCount);
    }
    @GetMapping("/getFormationUsers/{formationId}")
    public ResponseEntity<Map<String, Set<User>>> getFormationUsers(@PathVariable("formationId") Long formationId) {
        Set<User> users = this.formationService.getUsersForFormation(formationId);
        Map<String, Set<User>> response = new HashMap<>();
        response.put("users", users);
        return ResponseEntity.ok(response);
    }
}
