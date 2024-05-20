package com.StoryCraftBackend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.StoryCraftBackend.entity.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
    Optional<Role> findByRoleDescription(String roleDescription); // Corrected method
    List<Role> findByRoleNameContaining(String partialRoleName);

    // You can add more methods based on your requirements
}
