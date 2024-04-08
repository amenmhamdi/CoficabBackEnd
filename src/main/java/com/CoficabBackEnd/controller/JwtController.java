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
import com.CoficabBackEnd.dao.VerificationCodeDto;
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

    @PostMapping("/sendVerificationCode")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailDto emailDto) {
        String email = emailDto.getEmail();

        // Generate a random 4-digit code
        String code = jwtService.generateRandomCode();

        // Store the code with the email
        jwtService.storeVerificationCode(email, code);

        // Send the code to the user's email
        jwtService.sendLoginVerificationCodeEmail(email, code);

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("status", "success");
        responseMap.put("message", "Verification code sent!");

        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/verifyVerificationCode")
    public ResponseEntity<?> verifyVerificationCode(@RequestBody VerificationCodeDto verificationCodeDto) {
        String email = verificationCodeDto.getEmail();
        String code = verificationCodeDto.getCode();

        boolean isCodeValid = jwtService.verifyVerificationCode(email, code);

        Map<String, String> responseMap = new HashMap<>();
        if (isCodeValid) {
            responseMap.put("status", "success");
            responseMap.put("message", "Verification code is valid!");
        } else {
            responseMap.put("status", "error");
            responseMap.put("message", "Invalid verification code!");
        }

        return ResponseEntity.ok(responseMap);
    }

}
