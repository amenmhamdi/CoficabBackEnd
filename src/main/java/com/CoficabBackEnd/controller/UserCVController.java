package com.CoficabBackEnd.controller;

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

import com.CoficabBackEnd.entity.UserCV;
import com.CoficabBackEnd.service.UserCVService;

@RestController
@RequestMapping("/cv")
public class UserCVController {

    @Autowired
    private UserCVService userCVService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCV(@RequestParam("username") String username,
            @RequestParam("file") MultipartFile file) {
        try {
            userCVService.storeCV(username, file);
            return ResponseEntity.ok("CV uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload CV");
        }
    }

    // Endpoint to fetch the actual CV file content by ID
    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> getCVFile(@PathVariable("id") Long id) {
        try {
            byte[] fileContent = userCVService.getCVFileContent(id);
            if (fileContent != null) {
                ByteArrayResource resource = new ByteArrayResource(fileContent);

                // Get the file name from the database
                UserCV userCV = userCVService.getCVById(id);
                String filename = userCV.getFileName();

                // Determine the media type based on the file extension
                MediaType mediaType;
                if (filename.endsWith(".pdf")) {
                    mediaType = MediaType.APPLICATION_PDF;
                } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                    mediaType = MediaType.IMAGE_JPEG;
                } else if (filename.endsWith(".png")) {
                    mediaType = MediaType.IMAGE_PNG;
                } else {
                    // Default to octet-stream for unknown file types
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                }

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

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
    public ResponseEntity<String> deleteCV(@PathVariable("id") Long id) {
        try {
            // Call the service method to delete CV
            userCVService.deleteCV(id);
            return ResponseEntity.ok("CV deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete CV");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCV> getCVById(@PathVariable("id") Long id) {
        UserCV cv = userCVService.getCVById(id);
        if (cv != null) {
            return new ResponseEntity<>(cv, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<UserCV>> getCVsByUsername(@PathVariable("username") String username) {
        List<UserCV> cvs = userCVService.getCVsByUsername(username);
        return new ResponseEntity<>(cvs, HttpStatus.OK);
    }
}
