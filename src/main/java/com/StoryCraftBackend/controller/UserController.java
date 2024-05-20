package com.StoryCraftBackend.controller;

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

import com.StoryCraftBackend.dao.ChangePasswordRequest;
import com.StoryCraftBackend.entity.User;
import com.StoryCraftBackend.service.UserService;

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
    public User registerNewUser(@RequestBody User user) {
        return userService.registerNewUser(user);
    }

    @PutMapping("/assignRole/{username}/{roleName}")
    public ResponseEntity<String> assignRole(@PathVariable("username") String username,
            @PathVariable("roleName") String roleName) {
        try {
            userService.assignRole(username, roleName);
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

    @GetMapping("/{username}")
    public User getUser(@PathVariable("username") String username) {
        return this.userService.getUser(username);
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

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable("username") String username) {
        try {
            userService.deleteUser(username);
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

    @GetMapping("/checkEmailExists/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean emailExists = userService.isEmailExists(email);
        return ResponseEntity.ok(emailExists);
    }

    @PutMapping("/changePassword/{username}")
    public ResponseEntity<String> changePassword(@PathVariable("username") String username,
            @RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(username,
                    changePasswordRequest.getNewPassword(),
                    changePasswordRequest.getConfirmPassword());
            return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verifyOldPassword/{username}")
    public ResponseEntity<String> verifyOldPassword(@PathVariable("username") String username,
            @RequestBody String oldPassword) {
        boolean isOldPasswordCorrect = userService.verifyOldPassword(username, oldPassword);
        if (isOldPasswordCorrect) {
            return new ResponseEntity<>("Old password verified successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }
    }
}
