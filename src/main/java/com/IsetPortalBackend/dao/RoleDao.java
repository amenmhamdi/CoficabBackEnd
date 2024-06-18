package com.IsetPortalBackend.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.IsetPortalBackend.entity.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
    Optional<Role> findByRoleDescription(String roleDescription);
    List<Role> findByRoleNameContaining(String partialRoleName);
}