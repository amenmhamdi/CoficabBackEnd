package com.StoryCraftBackend.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.StoryCraftBackend.entity.Role;
import com.StoryCraftBackend.entity.User;
import com.StoryCraftBackend.entity.UserRole;
import com.StoryCraftBackend.repository.RoleRepository;
import com.StoryCraftBackend.repository.UserRepository;
import com.StoryCraftBackend.repository.UserRoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    // Create a new role
    public Role createNewRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRoleDescription(String roleName, Role newRoleData) {
        // Check if the role with roleName exists
        Role existingRole = roleRepository.findById(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Update role description
        existingRole.setRoleDescription(newRoleData.getRoleDescription());

        // Save the updated role
        return roleRepository.save(existingRole);
    }

    @Transactional
    public void deleteRole(String roleName) {
        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Retrieve UserRole associations with this role
        List<UserRole> userRoles = userRoleRepository.findByRole(role);

        // Remove role associations from users and set to default role if necessary
        Role defaultRole = roleRepository.findById("Reader")
                .orElseThrow(() -> new RuntimeException("Default role 'Reader' not found"));

        for (UserRole userRole : userRoles) {
            User user = userRole.getUser();
            userRoleRepository.delete(userRole);
            userRoleRepository.save(new UserRole(user, defaultRole));
        }

        // Finally, delete the role
        roleRepository.delete(role);
    }

    // Get all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Find a role by its name
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findById(roleName);
    }
}