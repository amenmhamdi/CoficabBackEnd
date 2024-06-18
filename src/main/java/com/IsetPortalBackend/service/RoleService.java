package com.IsetPortalBackend.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.IsetPortalBackend.entity.Role;
import com.IsetPortalBackend.entity.User;
import com.IsetPortalBackend.repository.RoleRepository;
import com.IsetPortalBackend.repository.UserRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public Role createNewRole(Role role) {
        // Check if role with the same name already exists
        if (roleRepository.existsByRoleName(role.getRoleName())) {
            throw new RuntimeException("Role with the same name already exists");
        }
        // If not, proceed with creating the role
        return roleRepository.save(role);
    }

    public Role updateRoleDescription(Long roleId, Role newRoleData) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    
        // Check if the updated role name conflicts with existing roles
        if (!existingRole.getRoleName().equals(newRoleData.getRoleName())) {
            if (roleRepository.existsByRoleName(newRoleData.getRoleName())) {
                throw new RuntimeException("Role with the updated name already exists");
            }
            existingRole.setRoleName(newRoleData.getRoleName());
        }
    
        // Update role description
        existingRole.setRoleDescription(newRoleData.getRoleDescription());
    
        // Save the updated role
        return roleRepository.save(existingRole);
    }

    @Transactional
    public void deleteRole(Long roleId) {
        Role roleToDelete = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Find default role
        Role defaultRole = roleRepository.findByRoleName("Reader")
                .orElseThrow(() -> new RuntimeException("Default role 'Reader' not found"));

        // Retrieve all users
        List<User> allUsers = userRepository.findAll();

        // Update each user with the default role if they have the role to be deleted
        for (User user : allUsers) {
            if (user.getRole().equals(roleToDelete)) {
                user.setRole(defaultRole);
                userRepository.save(user);
            }
        }

        // Delete the role after all users are updated
        roleRepository.delete(roleToDelete);
    }

    // Get all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Find a role by its ID
    public Optional<Role> getRoleById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    // Find a role by its name
    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
