package com.StoryCraftBackend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.StoryCraftBackend.entity.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, String> {
    Optional<Role> findByRoleName(String roleName);
    // Add additional methods as needed
    Optional<Role> findByRoleDescription(String roleDescription);
    List<Role> findByRoleNameContaining(String partialRoleName);

    // You can add more methods based on your requirements
}
