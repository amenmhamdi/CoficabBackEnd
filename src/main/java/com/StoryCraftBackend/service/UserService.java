package com.StoryCraftBackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.StoryCraftBackend.dao.UserDao;
import com.StoryCraftBackend.entity.ImageData;
import com.StoryCraftBackend.entity.Role;
import com.StoryCraftBackend.entity.User;
import com.StoryCraftBackend.repository.RoleRepository;
import com.StoryCraftBackend.repository.StorageRepository;
import com.StoryCraftBackend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageRepository repository;
    
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initRoleAndUser() {
        if (roleRepository.count() == 0) { // Check if roles exist in the database

            Role adminRole = new Role();
            adminRole.setRoleName("Admin");
            adminRole.setRoleDescription("Administrator with full access");
            adminRole.setCreatedAt(LocalDateTime.now());
            adminRole.setUpdatedAt(LocalDateTime.now());
            roleRepository.save(adminRole);

            Role editorRole = new Role();
            editorRole.setRoleName("Editor");
            editorRole.setRoleDescription("User with editorial responsibilities");
            editorRole.setCreatedAt(LocalDateTime.now());
            roleRepository.save(editorRole);

            Role moderateRole = new Role();
            moderateRole.setRoleName("Moderate");
            moderateRole.setRoleDescription(
                    "User with the ability to moderate user-generated content and community interactions");
            moderateRole.setCreatedAt(LocalDateTime.now());
            roleRepository.save(moderateRole);

            Role readerRole = new Role();
            readerRole.setRoleName("Reader");
            readerRole.setRoleDescription("Default role for new users");
            readerRole.setCreatedAt(LocalDateTime.now());
            readerRole.setUpdatedAt(LocalDateTime.now());
            roleRepository.save(readerRole);

            Role writerRole = new Role();
            writerRole.setRoleName("Writer");
            writerRole.setRoleDescription("User with the ability to write and publish stories");
            writerRole.setCreatedAt(LocalDateTime.now());
            roleRepository.save(writerRole);

            User adminUser = new User();
            adminUser.setUserName("admin123");
            adminUser.setUserPassword(getEncodedPassword("admin@pass"));
            adminUser.setUserFirstName("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setUserLastName("admin");
            adminUser.setEmailVerified(true);
            adminUser.setRole(adminRole);
            userRepository.save(adminUser);
        }
    }

    public User registerNewUser(User user) {
        // Check if the username already exists
        if (userRepository.existsById(user.getUserName())) {
            throw new RuntimeException("Username already exists");
        }

        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        user.setCreatedDate(LocalDateTime.now());
        user.setActive(true);

        Role defaultRole = roleRepository.findByRoleName("Reader")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    public void assignRole(String username, String roleName) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

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

    public User getUser(String username) {
        return userRepository.findByUserName(username);
    }

    public User updateUser(User updatedUser) {
        // Fetch the existing user from the database
        User existingUser = userRepository.findByUserName(updatedUser.getUserName());

        if (existingUser != null) {
            // Update the user fields
            existingUser.setUserFirstName(updatedUser.getUserFirstName());
            existingUser.setUserLastName(updatedUser.getUserLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setUserPassword(getEncodedPassword(updatedUser.getUserPassword()));
            existingUser.setBio(updatedUser.getBio());
            existingUser.setAvatar(updatedUser.getAvatar());
            existingUser.setNotificationsEnabled(updatedUser.isNotificationsEnabled());
            existingUser.setTheme(updatedUser.getTheme());
            existingUser.setWebsite(updatedUser.getWebsite());
            existingUser.setLocation(updatedUser.getLocation());
            existingUser.setSocialLinks(updatedUser.getSocialLinks());
            existingUser.setCreatedDate(updatedUser.getCreatedDate());
            existingUser.setLastLoginDate(updatedUser.getLastLoginDate());
            existingUser.setActive(updatedUser.isActive());
            existingUser.setPreferredGenres(updatedUser.getPreferredGenres());
            existingUser.setDefaultLanguage(updatedUser.getDefaultLanguage());
            existingUser.setFollowerCount(updatedUser.getFollowerCount());
            existingUser.setFollowingCount(updatedUser.getFollowingCount());
            existingUser.setAchievements(updatedUser.getAchievements());
            existingUser.setMembershipLevel(updatedUser.getMembershipLevel());

            // Update the ImageData association
            if (updatedUser.getImageData() != null) {
                // Ensure that the ImageData object is properly managed by Hibernate
                if (existingUser.getImageData() == null) {
                    // Set the ImageData from the updatedUser
                    existingUser.setImageData(updatedUser.getImageData());
                    // Set the User association in the ImageData
                    updatedUser.getImageData().setUser(existingUser);
                } else {
                    // Update the fields of the existing ImageData object
                    existingUser.getImageData().setName(updatedUser.getImageData().getName());
                    existingUser.getImageData().setType(updatedUser.getImageData().getType());
                    existingUser.getImageData().setImageData(updatedUser.getImageData().getImageData());
                }
            }

            // Save the updated user
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public void deleteUser(String userName) {
        User user = userRepository.findById(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete the associated ImageData record, if exists
        Optional<ImageData> imageDataOptional = repository.findByUserUserName(userName);
        imageDataOptional.ifPresent(imageData -> repository.delete(imageData));

        // Finally, delete the user
        userRepository.delete(user);
    }

    @Transactional
    public void deleteRole(String roleName) {
        Optional<Role> roleOptional = roleRepository.findByRoleName(roleName);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            roleRepository.delete(role);
        } else {
            throw new RuntimeException("Role not found");
        }
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

  
    public boolean isUsernameExists(String username) {
        return userDao.existsByUserName(username);
    }
    public void changePassword(String username, String newPassword, String confirmPassword) {
        // Retrieve the user from the database by username
        User user = userRepository.findByUserName(username);

        // Check if the user exists
        if (user != null) {
            // Check if the new password and the confirmed password match
            if (newPassword.equals(confirmPassword)) {
                // Encode the new password before saving it
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                // Set the new encoded password
                user.setUserPassword(encodedNewPassword);
                // Save the updated user with the new password
                userRepository.save(user);
            } else {
                throw new RuntimeException("New password and confirm password do not match");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public boolean verifyOldPassword(String username, String oldPassword) {
        // Retrieve the user from the database by username
        User user = userRepository.findByUserName(username);

        // Check if the user exists
        if (user != null) {
            // Verify if the old password matches the stored password
            return passwordEncoder.matches(oldPassword, user.getUserPassword());
        } else {
            // User not found, return false
            return false;
        }
    }
}
