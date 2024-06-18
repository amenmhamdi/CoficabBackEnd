package com.IsetPortalBackend.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.IsetPortalBackend.dao.UserDao;
import com.IsetPortalBackend.entity.JwtRequest;
import com.IsetPortalBackend.entity.JwtResponse;
import com.IsetPortalBackend.entity.Role;
import com.IsetPortalBackend.entity.User;
import com.IsetPortalBackend.repository.UserRepository;
import com.IsetPortalBackend.util.JwtUtil;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final org.jboss.logging.Logger logger = LoggerFactory.logger(JwtService.class);

    private Map<String, String> verificationCodes = new HashMap<>();

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String email = jwtRequest.getEmail();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(email, userPassword);
        UserDetails userDetails = loadUserByUsername(email);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return new JwtResponse(user, newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Set<SimpleGrantedAuthority> authorities = getAuthorities(user);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getUserPassword(), authorities);
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Role role = user.getRole();
        if (role != null) {
            return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        } else {
            return Collections.emptySet();
        }
    }

    private void authenticate(String email, String userPassword) throws Exception {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            if (user.isEmailVerified()) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, userPassword));
            } else {
                throw new DisabledException("USER_NOT_VERIFIED");
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public void sendPasswordResetToken(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
            userRepository.save(user);
            String resetLink = "http://localhost:4200/#/reset-password/" + token;
            emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        }
    }
    

    public void resetPassword(String token, String newPassword) {
        logger.info("Attempting password reset with token: " + token);
        User user = userRepository.findByResetToken(token).orElseThrow(() -> new RuntimeException("No user found with the provided reset token"));
        logger.info("User found for token: " + token);
        logger.info("Token expiry date: " + user.getResetTokenExpiry());
        if (isTokenValid(user.getResetTokenExpiry())) {
            logger.info("Token is valid. Resetting password for user: " + user.getEmail());
            user.setUserPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
            logger.info("Password reset successfully for user: " + user.getEmail());
        } else {
            logger.warn("Token is invalid or expired for user: " + user.getEmail());
            throw new RuntimeException("Invalid or expired token");
        }
    }

    private boolean isTokenValid(LocalDateTime tokenExpiry) {
        return tokenExpiry != null && tokenExpiry.isAfter(LocalDateTime.now());
    }

    public void sendLoginVerificationCode(String email) {
        String code = generateRandomCode();
        verificationCodes.put(email, code);
        emailService.sendLoginVerificationCodeEmail(email, code);
    }

    public String generateRandomCode() {
        return String.valueOf(1000 + new Random().nextInt(9000));
    }

    public boolean verifyVerificationCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        return code.equals(storedCode);
    }

    public void storeVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
    }

    public void sendLoginVerificationCodeEmail(String email, String code) {
        emailService.sendLoginVerificationCodeEmail(email, code);
    }
}
