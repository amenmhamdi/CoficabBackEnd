package com.CoficabBackEnd.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.entity.UserCV;
import com.CoficabBackEnd.repository.UserCVRepository;
import com.CoficabBackEnd.repository.UserRepository;

@Service
public class UserCVService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCVRepository userCVRepository;

    // Directory where CV files will be stored
    private static final String CV_UPLOAD_DIR = "src/main/resources/cv/";

    public void storeCV(String username, MultipartFile file) {
        try {
            User user = userRepository.findByUserName(username);
            if (user != null) {
                // Calculate file size
                long fileSize = file.getSize();

                UserCV existingCV = user.getCv();
                if (existingCV != null) {
                    // Delete the existing CV file
                    deleteCVFile(username, existingCV.getFileName());

                    // Update the existing CV record in the database
                    existingCV.setFileName(file.getOriginalFilename());
                    existingCV.setFileSize(fileSize); // Set the file size
                    existingCV.setTimestamp(LocalDateTime.now()); // Set the timestamp to the current time
                    userCVRepository.save(existingCV);
                } else {
                    // Check if CV already exists in the database
                    UserCV existingUserCV = userCVRepository.findByUser(user);
                    if (existingUserCV != null) {
                        // If CV exists, update its filename and size
                        existingUserCV.setFileName(file.getOriginalFilename());
                        existingUserCV.setFileSize(fileSize); // Set the file size
                        existingUserCV.setTimestamp(LocalDateTime.now()); // Set the timestamp to the current time
                        userCVRepository.save(existingUserCV);
                    } else {
                        // Create a new CV entry in the database
                        UserCV userCV = new UserCV();
                        userCV.setUser(user);
                        userCV.setFileName(file.getOriginalFilename());
                        userCV.setFileSize(fileSize); // Set the file size
                        userCV.setTimestamp(LocalDateTime.now()); // Set the timestamp to the current time
                        userCVRepository.save(userCV);
                    }
                }

                // Create a directory for the user if it doesn't exist
                Path userDirectory = Paths.get(CV_UPLOAD_DIR, username);
                if (!Files.exists(userDirectory)) {
                    Files.createDirectories(userDirectory);
                }

                // Save the CV file to the user's directory
                byte[] bytes = file.getBytes();
                Path filePath = Paths.get(userDirectory.toString(), file.getOriginalFilename());
                Files.write(filePath, bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file storage exception
        }
    }

    private void deleteCVFile(String username, String fileName) throws IOException {
        Path path = Paths.get(CV_UPLOAD_DIR, username, fileName);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    // Method to delete the actual CV file and its entry from the database
    public void deleteCV(Long id) {
        UserCV userCV = userCVRepository.findById(id).orElse(null);
        if (userCV != null) {
            String fileName = userCV.getFileName();
            String username = userCV.getUser().getUserName();
            try {
                // Delete the file from the file system
                deleteCVFile(username, fileName);
                // Delete the entry from the database
                userCVRepository.delete(userCV);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle file deletion exception
            }
        }
    }

    // Method to fetch the actual CV file content
    public byte[] getCVFileContent(Long id) throws IOException {
        UserCV userCV = userCVRepository.findById(id).orElse(null);
        if (userCV != null) {
            String fileName = userCV.getFileName();
            String username = userCV.getUser().getUserName(); // Get the username
            Path filePath = Paths.get(CV_UPLOAD_DIR, username, fileName); // Include username in the file path
            return Files.readAllBytes(filePath);
        }
        return null;
    }

    public List<UserCV> getAllCVs() {
        return userCVRepository.findAll();
    }

    public List<Object[]> getAllCVsWithUsername() {
        return userCVRepository.findAllWithUsername();
    }

    public UserCV getCVById(Long id) {
        return userCVRepository.findById(id).orElse(null);
    }

    public List<UserCV> getCVsByUsername(String username) {
        return userCVRepository.findByUserUserName(username);
    }

    // Additional methods for retrieving and managing CVs can be added here
}
