package com.IsetPortalBackend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.IsetPortalBackend.entity.UserImage;
import com.IsetPortalBackend.service.UserImageService;

@RestController
@RequestMapping("/image")
public class UserImageController {

    @Autowired
    private UserImageService userImageService;

    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadImage(@PathVariable("userId") Long userId, @RequestParam("file") MultipartFile file) {
        try {
            userImageService.storeImage(userId, file);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> getImageFile(@PathVariable("id") Long id) {
        try {
            byte[] fileContent = userImageService.getImageFileContent(id);
            if (fileContent != null) {
                ByteArrayResource resource = new ByteArrayResource(fileContent);

                UserImage userImage = userImageService.getImageById(id);
                String filename = userImage.getName();

                MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
                if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                    mediaType = MediaType.IMAGE_JPEG;
                } else if (filename.endsWith(".png")) {
                    mediaType = MediaType.IMAGE_PNG;
                }

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(fileContent.length)
                        .contentType(mediaType)
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable("id") Long id) {
        try {
            userImageService.deleteImage(id);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserImage> getImageById(@PathVariable("id") Long id) {
        UserImage image = userImageService.getImageById(id);
        if (image != null) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserImage>> getImagesByUserId(@PathVariable("userId") Long userId) {
        List<UserImage> images = userImageService.getImagesByUserId(userId);
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserImage>> getAllImages() {
        List<UserImage> images = userImageService.getAllImages();
        return new ResponseEntity<>(images, HttpStatus.OK);
    }
}
