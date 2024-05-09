package com.CoficabBackEnd.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                UserCV existingCV = user.getCv();
                if (existingCV != null) {
                    // Delete the existing CV file
                    deleteCVFile(existingCV.getFileName());
    
                    // Update the existing CV record in the database
                    existingCV.setFileName(file.getOriginalFilename());
                    userCVRepository.save(existingCV);
                } else {
                    // Check if CV already exists in the database
                    UserCV existingUserCV = userCVRepository.findByUser(user);
                    if (existingUserCV != null) {
                        // If CV exists, update its filename
                        existingUserCV.setFileName(file.getOriginalFilename());
                        userCVRepository.save(existingUserCV);
                    } else {
                        // Create a new CV entry in the database
                        UserCV userCV = new UserCV();
                        userCV.setUser(user);
                        userCV.setFileName(file.getOriginalFilename());
                        userCVRepository.save(userCV);
                    }
                }
    
                // Create the directory if it doesn't exist
                Path directory = Paths.get(CV_UPLOAD_DIR);
                if (!Files.exists(directory)) {
                    Files.createDirectories(directory);
                }
    
                // Save the CV file to the specified directory
                byte[] bytes = file.getBytes();
                Path path = Paths.get(directory.toString(), file.getOriginalFilename());
                Files.write(path, bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file storage exception
        }
    }
    
    

    private void deleteCVFile(String fileName) throws IOException {
        Path path = Paths.get(CV_UPLOAD_DIR, fileName);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    // Method to delete the actual CV file and its entry from the database
    public void deleteCV(Long id) {
        UserCV userCV = userCVRepository.findById(id).orElse(null);
        if (userCV != null) {
            String fileName = userCV.getFileName();
            Path filePath = Paths.get(CV_UPLOAD_DIR, fileName);
            try {
                // Delete the file from the file system
                Files.deleteIfExists(filePath);
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
            Path filePath = Paths.get(CV_UPLOAD_DIR, fileName);
            return Files.readAllBytes(filePath);
        }
        return null;
    }

    public UserCV getCVById(Long id) {
        return userCVRepository.findById(id).orElse(null);
    }

    public List<UserCV> getCVsByUsername(String username) {
        return userCVRepository.findByUserUserName(username);
    }

    // Additional methods for retrieving and managing CVs can be added here
}
