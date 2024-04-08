package com.CoficabBackEnd.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.service.UserService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initRoleAndUser() {
        userService.initRoleAndUser();
    }

    @PostMapping({ "/registerNewUser" })
    public User registerNewUser(@RequestBody User user) {
        return userService.registerNewUser(user);
    }

    @PutMapping("/assignRole")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> assignRole(@RequestBody User user) {
        try {
            userService.assignRole(user);
            return new ResponseEntity<>("Role assigned successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        // Perform the logic to update the user in the database
        User updated = userService.updateUser(updatedUser);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable("username") String username) {
        return this.userService.getUser(username);
    }

    @DeleteMapping("/role/{roleName}")
    public void deleteRole(@PathVariable("roleName") String roleName) {
        userService.deleteRole(roleName);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable("username") String username) {
        userService.deleteUser(username);
    }

    @GetMapping({ "/forAdmin" })
    @PreAuthorize("hasRole('Admin')")
    public String forAdmin() {
        return "This URL is only accessible to the admin";
    }

    @GetMapping({ "/forUser" })
    @PreAuthorize("hasRole('User')")
    public String forUser() {
        return "This URL is only accessible to the user";
    }

    @GetMapping({ "/getallusers" })
    public List<User> getAllUsers() {
        return userService.GetAllUsers();
    }

    @PutMapping("/updateverif/{username}")
    @PreAuthorize("hasRole('Admin') or hasRole('HR-manager')")
    public ResponseEntity<String> updateVerif(@PathVariable("username") String username) {
        try {
            userService.updateVerif(username);
            return new ResponseEntity<>("Verification status updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateUnverif/{username}")
    @PreAuthorize("hasRole('Admin') or hasRole('HR-manager')")
    public ResponseEntity<String> updateUnverif(@PathVariable("username") String username) {
        try {
            userService.updateUnverif(username);
            return new ResponseEntity<>("Verification status updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUnverifiedUsers")
    public List<User> getUnverifiedUsers() {
        return userService.getUnverifiedUsers();
    }

    @GetMapping("/getVerifiedUsers")
    public List<User> getVerifiedUsers() {
        return userService.getVerifiedUsers();
    }

    @GetMapping("/checkEmailExists/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean emailExists = userService.isEmailExists(email);
        return ResponseEntity.ok(emailExists);
    }

}
