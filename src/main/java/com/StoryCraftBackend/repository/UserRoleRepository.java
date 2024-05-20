package com.StoryCraftBackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.StoryCraftBackend.entity.Role;
import com.StoryCraftBackend.entity.User;
import com.StoryCraftBackend.entity.UserRole;
import com.StoryCraftBackend.entity.UserRoleId;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByUser(User user);
    List<UserRole> findByRole(Role role);
    UserRole findByUserAndRole(User user, Role role);

}
