package com.IsetPortalBackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.IsetPortalBackend.entity.Role;
import com.IsetPortalBackend.entity.User;
import com.IsetPortalBackend.entity.UserImage;
import com.IsetPortalBackend.repository.RoleRepository;
import com.IsetPortalBackend.repository.StorageRepository;
import com.IsetPortalBackend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initRoleAndUser() {
        if (roleRepository.count() == 0) {
            createRole("Admin", "Administrator with full access");
            createRole("Student", "Role for students");
            createRole("Professor", "Role for professors");

            Role adminRole = roleRepository.findByRoleName("Admin").orElseThrow(() -> new RuntimeException("Admin role not found"));
            
            User adminUser = new User();
            adminUser.setUserPassword(getEncodedPassword("admin@pass"));
            adminUser.setUserFirstName("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setUserLastName("admin");
            adminUser.setEmailVerified(true);
            adminUser.setRole(adminRole);
            userRepository.save(adminUser);
        }
    }

    private void createRole(String roleName, String roleDescription) {
        Role role = new Role();
        role.setRoleName(roleName);
        role.setRoleDescription(roleDescription);
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());
        roleRepository.save(role);
    }

    public void updateLoginDetails(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLoginDate(LocalDateTime.now());
        user.setActive(true); // Mark user as active on login
        userRepository.save(user);
    }

    public void setUserInactive(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false); // Mark user as inactive on logout
        userRepository.save(user);
    }

    public User registerNewUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        user.setCreatedDate(LocalDateTime.now());

        Role role;
        if (user.getRole() == null) {
            role = roleRepository.findByRoleName("Student")
                    .orElseThrow(() -> new RuntimeException("Default role not found"));
        } else {
            role = roleRepository.findByRoleName(user.getRole().getRoleName())
                    .orElseThrow(() -> new RuntimeException("Specified role not found"));
        }

        user.setRole(role);

        return userRepository.save(user);
    }

    public void updateLastLogin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);
    }

    public void assignRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);
        userRepository.save(user);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(User updatedUser) {
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setUserFirstName(updatedUser.getUserFirstName());
        existingUser.setUserLastName(updatedUser.getUserLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setUserPassword(getEncodedPassword(updatedUser.getUserPassword()));
        existingUser.setBio(updatedUser.getBio());
        existingUser.setNotificationsEnabled(updatedUser.isNotificationsEnabled());
        existingUser.setTheme(updatedUser.getTheme());
        existingUser.setWebsite(updatedUser.getWebsite());
        existingUser.setSocialLinks(updatedUser.getSocialLinks());
        existingUser.setCreatedDate(updatedUser.getCreatedDate());
        existingUser.setLastLoginDate(updatedUser.getLastLoginDate());
        existingUser.setActive(updatedUser.isActive());
        existingUser.setDefaultLanguage(updatedUser.getDefaultLanguage());
        existingUser.setAddress(updatedUser.getAddress());

        if (updatedUser.getUserImage() != null) {
            if (existingUser.getUserImage() == null) {
                UserImage newUserImage = new UserImage();
                newUserImage.setName(updatedUser.getUserImage().getName());
                newUserImage.setType(updatedUser.getUserImage().getType());
                newUserImage.setUser(existingUser);
                existingUser.setUserImage(newUserImage);
            } else {
                existingUser.getUserImage().setName(updatedUser.getUserImage().getName());
                existingUser.getUserImage().setType(updatedUser.getUserImage().getType());
            }
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete the associated ImageData record, if exists
        Optional<UserImage> imageDataOptional = repository.findByUserId(userId);
        imageDataOptional.ifPresent(imageData -> repository.delete(imageData));

        // Finally, delete the user
        userRepository.delete(user);
    }

    @Transactional
    public void deleteRole(String roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roleRepository.delete(role);
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public void changePassword(Long userId, String newPassword, String confirmPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (newPassword.equals(confirmPassword)) {
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setUserPassword(encodedNewPassword);
            userRepository.save(user);
        } else {
            throw new RuntimeException("New password and confirm password do not match");
        }
    }

    public boolean verifyOldPassword(Long userId, String oldPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return passwordEncoder.matches(oldPassword, user.getUserPassword());
    }

    public void updateNotificationPreference(Long userId, boolean notificationsEnabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setNotificationsEnabled(notificationsEnabled);
        userRepository.save(user);
    }
}
