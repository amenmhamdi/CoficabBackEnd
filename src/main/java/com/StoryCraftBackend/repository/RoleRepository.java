package com.StoryCraftBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StoryCraftBackend.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByRoleName(String roleName);
}
