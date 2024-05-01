package com.CoficabBackEnd.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.CoficabBackEnd.dao.RoleDao;
import com.CoficabBackEnd.dao.UserDao;
import com.CoficabBackEnd.entity.Formation;
import com.CoficabBackEnd.entity.FormationComment;
import com.CoficabBackEnd.entity.ImageData;
import com.CoficabBackEnd.entity.Notification;
import com.CoficabBackEnd.entity.Role;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.repository.FormationCommentRepository;
import com.CoficabBackEnd.repository.FormationRepository;
import com.CoficabBackEnd.repository.NotificationRepository;
import com.CoficabBackEnd.repository.RoleRepository;
import com.CoficabBackEnd.repository.StorageRepository;
import com.CoficabBackEnd.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageRepository repository;

    @Autowired
    private FormationRepository formationRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FormationCommentRepository formationCommentRepository;

    public void initRoleAndUser() {
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly created record");
        roleDao.save(userRole);

        User adminUser = new User();
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        adminUser.setUserFirstName("admin");
        adminUser.setEmail("mhamdiamenallah666@gmail.com");
        adminUser.setUserLastName("admin");
        adminUser.setVerif(true);
        adminUser.setRole(adminRole); // Assigning the admin role directly
        userDao.save(adminUser);
    }

    public User registerNewUser(User user) {
        // Check if the username already exists
        if (userRepository.existsById(user.getUserName())) {
            throw new RuntimeException("Username already exists");
        }

        Role defaultRole = roleDao.findById("User")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(defaultRole); // Assigning the default role directly
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));

        return userDao.save(user);
    }

    public void assignRole(User user) {
        String username = user.getUserName();
        String roleName = user.getRole().getRoleName(); // Assuming Role is already set in the User object

        User existingUser = userRepository.findByUserName(username);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        Role role = roleDao.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        existingUser.setRole(role);
        userRepository.save(existingUser);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public List<User> GetAllUsers() {
        return userRepository.findAll();
    }

    // Get list of unverified users
    public List<User> getUnverifiedUsers() {
        return userRepository.findAll().stream()
                .filter(user -> !user.isVerif())
                .collect(Collectors.toList());
    }

    // getting user by username
    public User getUser(String username) {

        return this.userRepository.findByUserName(username);
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
            existingUser.setUserPassword(updatedUser.getUserPassword());
            existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setJobTitle(updatedUser.getJobTitle());
            existingUser.setAboutMe(updatedUser.getAboutMe());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setDepartment(updatedUser.getDepartment());
            existingUser.setEmployeeId(updatedUser.getEmployeeId());
            existingUser.setHireDate(updatedUser.getHireDate());
            existingUser.setExperience(updatedUser.getExperience());
            existingUser.setSocialMediaLinks(updatedUser.getSocialMediaLinks());

            // Update the role only if it's not null in the updatedUser object
            if (updatedUser.getRole() != null) {
                // Ensure that the role is already persisted in the database
                Role persistedRole = roleRepository.findByRoleName(updatedUser.getRole().getRoleName());
                if (persistedRole != null) {
                    existingUser.setRole(persistedRole);
                } else {
                    throw new RuntimeException("Role not found");
                }
            }

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

    public void deleteRole(String roleName) {
        Role role = roleDao.findById(roleName).orElseThrow(null);
        roleDao.delete(role);
    }

    @Transactional
    public void deleteUser(String userName) {
        User user = userRepository.findById(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete notifications sent by the user
        List<Notification> sentNotifications = notificationRepository.findBySender_UserName(userName);
        notificationRepository.deleteAll(sentNotifications);

        // Delete notifications received by the user
        List<Notification> receivedNotifications = notificationRepository.findByReceiver_UserName(userName);
        notificationRepository.deleteAll(receivedNotifications);

        // Delete formation comments created by the user
        List<FormationComment> userFormationComments = formationCommentRepository.findByUserUserName(userName);
        formationCommentRepository.deleteAll(userFormationComments);

        // Remove the user from all associated formations
        for (Formation formation : user.getFormations()) {
            formation.getUsers().remove(user);
            formationRepository.save(formation);
        }

        // Delete the associated ImageData record, if exists
        Optional<ImageData> imageDataOptional = repository.findByUserUserName(userName);
        imageDataOptional.ifPresent(imageData -> repository.delete(imageData));

        // Finally, delete the user
        userRepository.delete(user);
    }

    public void updateVerif(String username) {
        // Récupérez l'utilisateur de la base de données par son nom d'utilisateur
        User user = userRepository.findByUserName(username);

        // Vérifiez si l'utilisateur existe
        if (user != null) {
            // Mettez à jour la variable verif
            user.setVerif(true);

            // Enregistrez la mise à jour dans la base de données
            userRepository.save(user);
        } else {
            // Gérez le cas où l'utilisateur n'existe pas
            throw new RuntimeException("User not found");
        }
    }

    public void updateUnverif(String username) {
        // Get the user from the database by username
        User user = userRepository.findByUserName(username);

        // Check if the user exists
        if (user != null) {
            // Update the verification status to false
            user.setVerif(false);

            // Save the update in the database
            userRepository.save(user);
        } else {
            // Handle the case where the user does not exist
            throw new RuntimeException("User not found");
        }
    }

    public List<User> getVerifiedUsers() {
        return userRepository.findAll().stream()
                .filter(User::isVerif)
                .peek(user -> user.setVerif(true)) // Make sure this line is present
                .collect(Collectors.toList());
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
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
