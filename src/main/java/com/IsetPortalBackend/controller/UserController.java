package com.IsetPortalBackend.controller;

import java.util.List;
import java.util.Map;

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

import com.IsetPortalBackend.dao.ChangePasswordRequest;
import com.IsetPortalBackend.entity.User;
import com.IsetPortalBackend.service.UserService;

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

    @PostMapping("/registerNewUser")
    public ResponseEntity<User> registerNewUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerNewUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignRole/{userId}/{roleName}")
    public ResponseEntity<String> assignRole(@PathVariable("userId") Long userId,
                                             @PathVariable("roleName") String roleName) {
        try {
            userService.assignRole(userId, roleName);
            return new ResponseEntity<>("Role assigned successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        User updated = userService.updateUser(updatedUser);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable("userId") Long userId) {
        return this.userService.getUser(userId);
    }

    @DeleteMapping("/role/{roleName}")
    public ResponseEntity<String> deleteRole(@PathVariable("roleName") String roleName) {
        try {
            userService.deleteRole(roleName);
            return new ResponseEntity<>("Role deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/forAdmin")
    @PreAuthorize("hasRole('Admin')")
    public String forAdmin() {
        return "This URL is only accessible to the admin";
    }

    @GetMapping("/forEditor")
    @PreAuthorize("hasRole('Editor')")
    public String forEditor() {
        return "This URL is only accessible to the Editor";
    }

    @GetMapping("/forUser")
    @PreAuthorize("hasRole('User')")
    public String forUser() {
        return "This URL is only accessible to the user";
    }

    @GetMapping("/getallusers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/updateLoginDetails/{userId}")
    public ResponseEntity<?> updateLoginDetails(@PathVariable Long userId) {
        try {
            userService.updateLoginDetails(userId);
            return ResponseEntity.ok("Login details updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update login details");
        }
    }

    @PutMapping("/logout/{userId}")
    public ResponseEntity<?> logout(@PathVariable Long userId) {
        try {
            userService.setUserInactive(userId);
            return ResponseEntity.ok("User logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to log out user");
        }
    }

    @PutMapping("/updateLastLogin/{userId}")
    public ResponseEntity<?> updateLastLogin(@PathVariable Long userId) {
        try {
            userService.updateLastLogin(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update last login date");
        }
    }

    @GetMapping("/checkEmailExists/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean emailExists = userService.isEmailExists(email);
        return ResponseEntity.ok(emailExists);
    }

    @GetMapping("/phone-exists/{phone}")
    public ResponseEntity<Boolean> isPhoneExists(@PathVariable String phone) {
        boolean exists = userService.isPhoneExists(phone);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/changePassword/{userId}")
    public ResponseEntity<String> changePassword(@PathVariable("userId") Long userId,
                                                 @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(userId,
                    changePasswordRequest.getNewPassword(),
                    changePasswordRequest.getConfirmPassword());
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verifyOldPassword/{userId}")
    public ResponseEntity<String> verifyOldPassword(@PathVariable("userId") Long userId,
                                                    @RequestBody String oldPassword) {
        boolean isOldPasswordCorrect = userService.verifyOldPassword(userId, oldPassword);
        if (isOldPasswordCorrect) {
            return new ResponseEntity<>("Old password verified successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateNotificationPreference")
    public ResponseEntity<String> updateNotificationPreference(@RequestBody Map<String, Object> request) {
        try {
            Long userId = ((Number) request.get("userId")).longValue();
            boolean notificationsEnabled = (Boolean) request.get("notificationsEnabled");
            userService.updateNotificationPreference(userId, notificationsEnabled);
            return new ResponseEntity<>("Notification preference updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
