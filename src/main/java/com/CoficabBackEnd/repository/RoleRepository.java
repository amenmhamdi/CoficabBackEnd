package com.CoficabBackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CoficabBackEnd.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByRoleName(String roleName);
}
