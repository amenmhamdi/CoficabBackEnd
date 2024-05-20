package com.StoryCraftBackend.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.StoryCraftBackend.entity.ImageData;
import com.StoryCraftBackend.entity.User;
import com.StoryCraftBackend.repository.StorageRepository;
import com.StoryCraftBackend.repository.UserRepository;
import com.StoryCraftBackend.util.ImageUtils;

@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    public String uploadImageWithUsername(String username, MultipartFile file) throws IOException {
        // Get the user by username
        User user = userRepository.findByUserName(username);
        if (user == null) {
            // Handle case when user is not found
            return "User not found with username: " + username;
        }

        // Delete previous image associated with the user, if exists
        Optional<ImageData> existingImageData = repository.findByUserUserName(username);
        existingImageData.ifPresent(imageData -> repository.delete(imageData));

        // Generate a unique filename based on the current user's username and the
        // original filename
        String uniqueFileName = username + "_" + file.getOriginalFilename();
        ImageData imageData = ImageData.builder()
                .name(uniqueFileName)
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .user(user) // Set the user
                .build();

        repository.save(imageData);

        return "File uploaded successfully: " + uniqueFileName;
    }

    public byte[] downloadImageByUsername(String username) {
        Optional<ImageData> dbImageData = repository.findByUserUserName(username);
        if (dbImageData.isPresent()) {
            return ImageUtils.decompressImage(dbImageData.get().getImageData());
        } else {
            // Handle case when no image is found for the user
            return null;
        }
    }

    public void deleteImageDataByUser(String username) {
        Optional<ImageData> imageDataOptional = repository.findByUserUserName(username);
        if (imageDataOptional.isPresent()) {
            repository.delete(imageDataOptional.get());
        }
    }
}
