package com.StoryCraftBackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.StoryCraftBackend.entity.Role;
import com.StoryCraftBackend.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByRole(Role role);

    User findByUserName(String userName);

    List<User> findByUserNameIn(List<String> userNames);

    List<User> findByEmail(String email);

    User findByResetToken(String resetToken);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);



}
