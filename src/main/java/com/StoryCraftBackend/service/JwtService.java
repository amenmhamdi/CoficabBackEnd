package com.StoryCraftBackend.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.StoryCraftBackend.dao.UserDao;
import com.StoryCraftBackend.entity.JwtRequest;
import com.StoryCraftBackend.entity.JwtResponse;
import com.StoryCraftBackend.entity.User;
import com.StoryCraftBackend.entity.UserRole;
import com.StoryCraftBackend.repository.UserRepository;
import com.StoryCraftBackend.repository.UserRoleRepository;
import com.StoryCraftBackend.util.JwtUtil;

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
    private UserRoleRepository userRoleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final org.jboss.logging.Logger logger = LoggerFactory.logger(JwtService.class);
    private Map<String, String> verificationCodes = new HashMap<>();

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();
        authenticate(userName, userPassword);

        UserDetails userDetails = loadUserByUsername(userName);
        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        User user = userDao.findById(userName).get();
        return new JwtResponse(user, newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user != null) {
            Set<SimpleGrantedAuthority> authorities = getAuthorities(user);
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    authorities);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        return userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRoleName()))
                .collect(Collectors.toSet());
    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            // Retrieve the user from the database
            User user = userRepository.findByUserName(userName);

            // Check if the user exists and is verified
            if (user != null && user.isEmailVerified()) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
            } else {
                // If the user doesn't exist or isn't verified, throw an appropriate exception
                throw new DisabledException("USER_NOT_VERIFIED");
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public void sendPasswordResetToken(String email) {
        List<User> users = userRepository.findByEmail(email);

        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                String token = UUID.randomUUID().toString();

                user.setResetToken(token);
                user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));

                userRepository.save(user);

                String resetLink = "http://localhost:4200/#/reset-password/" + token;

                emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
            }
        }
    }

    public void resetPassword(String token, String newPassword) {
        logger.info("Attempting password reset with token: " + token);

        User user = userRepository.findByResetToken(token);

        if (user != null) {
            logger.info("User found for token: " + token);
            logger.info("Token expiry date: " + user.getResetTokenExpiry());

            if (isTokenValid(user.getResetTokenExpiry())) {
                logger.info("Token is valid. Resetting password for user: " + user.getUserName());

                user.setUserPassword(passwordEncoder.encode(newPassword));
                user.setResetToken(null);
                user.setResetTokenExpiry(null);

                userRepository.save(user);
                logger.info("Password reset successfully for user: " + user.getUserName());
            } else {
                logger.warn("Token is invalid or expired for user: " + user.getUserName());
                throw new RuntimeException("Invalid or expired token");
            }
        } else {
            logger.warn("No user found with token: " + token);
            throw new RuntimeException("No user found with the provided reset token");
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

    // New method to store verification code
    public void storeVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
    }

    // New method to send verification code email
    public void sendLoginVerificationCodeEmail(String email, String code) {
        emailService.sendLoginVerificationCodeEmail(email, code);
    }
}
