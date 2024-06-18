package com.IsetPortalBackend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.IsetPortalBackend.entity.User;
import com.IsetPortalBackend.entity.UserImage;
import com.IsetPortalBackend.repository.UserImageRepository;
import com.IsetPortalBackend.repository.UserRepository;

@Service
public class UserImageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    private static final String IMAGE_UPLOAD_DIR = "src/main/resources/images/";

    public void storeImage(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            long fileSize = file.getSize();

            UserImage existingImage = userImageRepository.findByUser(user).orElse(null);
            if (existingImage != null) {
                deleteImageFile(userId, existingImage.getName());
                userImageRepository.delete(existingImage);
            }

            String uniqueFileName = userId + "_" + file.getOriginalFilename();
            UserImage userImage = UserImage.builder()
                    .user(user)
                    .name(uniqueFileName)
                    .size(fileSize)
                    .type(file.getContentType())
                    .timestamp(LocalDateTime.now())
                    .build();

            userImageRepository.save(userImage);

            Path userDirectory = Paths.get(IMAGE_UPLOAD_DIR, String.valueOf(userId));
            if (!Files.exists(userDirectory)) {
                Files.createDirectories(userDirectory);
            }

            Path filePath = Paths.get(userDirectory.toString(), uniqueFileName);
            Files.write(filePath, file.getBytes());
        }
    }

    private void deleteImageFile(Long userId, String fileName) throws IOException {
        Path path = Paths.get(IMAGE_UPLOAD_DIR, String.valueOf(userId), fileName);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }

    public void deleteImage(Long id) throws IOException {
        UserImage userImage = userImageRepository.findById(id).orElse(null);
        if (userImage != null) {
            deleteImageFile(userImage.getUser().getId(), userImage.getName());
            userImageRepository.delete(userImage);
        }
    }

    public byte[] getImageFileContent(Long id) throws IOException {
        UserImage userImage = userImageRepository.findById(id).orElse(null);
        if (userImage != null) {
            Path filePath = Paths.get(IMAGE_UPLOAD_DIR, String.valueOf(userImage.getUser().getId()), userImage.getName());
            return Files.readAllBytes(filePath);
        }
        return null;
    }

    public List<UserImage> getAllImages() {
        return userImageRepository.findAll();
    }

    public UserImage getImageById(Long id) {
        return userImageRepository.findById(id).orElse(null);
    }

    public List<UserImage> getImagesByUserId(Long userId) {
        return userImageRepository.findByUser_Id(userId);
    }
}
