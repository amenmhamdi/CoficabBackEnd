package com.StoryCraftBackend.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.StoryCraftBackend.service.StorageService;;

@RestController
@RequestMapping("/image")
public class StorageController {

    @Autowired
    private StorageService service;

    @PostMapping("/upload/{username}")
    public ResponseEntity<?> uploadImage(@PathVariable String username, @RequestParam("image") MultipartFile file)
            throws IOException {
        String uploadImage = service.uploadImageWithUsername(username, file);
        if (uploadImage != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(uploadImage);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image for user: " + username);
        }
    }

    @GetMapping("/download/{username}")
    public ResponseEntity<?> downloadImage(@PathVariable String username) {
        byte[] imageData = service.downloadImageByUsername(username);
        if (imageData != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Image not found for user: " + username);
        }
    }
}
