package com.CoficabBackEnd.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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

import com.CoficabBackEnd.dao.UserDao;
import com.CoficabBackEnd.entity.JwtRequest;
import com.CoficabBackEnd.entity.JwtResponse;
import com.CoficabBackEnd.entity.Role;
import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.repository.UserRepository;
import com.CoficabBackEnd.util.JwtUtil;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userrepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final org.jboss.logging.Logger logger = LoggerFactory.logger(JwtService.class);
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
        User user = userDao.findById(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthority(user));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        Role role = user.getRole();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        return authorities;
    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            // Récupérez l'utilisateur de la base de données
            User user = userrepository.findByUserName(userName);

            // Vérifiez si l'utilisateur existe et est vérifié
            if (user != null && user.isVerif()) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
            } else {
                // Si l'utilisateur n'existe pas ou n'est pas vérifié, lancez une exception
                // appropriée
                throw new DisabledException("USER_NOT_VERIFIED");
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public void sendPasswordResetToken(String email) {
        List<User> users = userrepository.findByEmail(email);

        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                String token = UUID.randomUUID().toString();

                user.setResetToken(token);
                user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));

                userrepository.save(user);

                String resetLink = "http://localhost:4200/#/reset-password/" + token;

                emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
            }
        }
    }

    public void resetPassword(String token, String newPassword) {
        logger.info("Attempting password reset with token: " + token);

        User user = userrepository.findByResetToken(token);

        if (user != null) {
            logger.info("User found for token: " + token);
            logger.info("Token expiry date: " + user.getResetTokenExpiry());

            if (isTokenValid(user.getResetTokenExpiry())) {
                logger.info("Token is valid. Resetting password for user: " + user.getUserName());

                user.setUserPassword(passwordEncoder.encode(newPassword));
                user.setResetToken(null);
                user.setResetTokenExpiry(null);

                userrepository.save(user);
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
}
