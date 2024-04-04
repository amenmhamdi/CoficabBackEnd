package com.CoficabBackEnd.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.CoficabBackEnd.dao.EmailDto;
import com.CoficabBackEnd.dao.PasswordResetDto;
import com.CoficabBackEnd.entity.JwtRequest;
import com.CoficabBackEnd.entity.JwtResponse;
import com.CoficabBackEnd.service.JwtService;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping({ "/authenticate" })
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }

    @PostMapping("/requestPasswordReset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody EmailDto emailDto) {
        jwtService.sendPasswordResetToken(emailDto.getEmail());

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("status", "success");
        responseMap.put("message", "Email sent!");

        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        jwtService.resetPassword(passwordResetDto.getToken(), passwordResetDto.getNewPassword());

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("status", "success");
        responseMap.put("message", "Password reset successful!");

        return ResponseEntity.ok(responseMap);
    }
}
