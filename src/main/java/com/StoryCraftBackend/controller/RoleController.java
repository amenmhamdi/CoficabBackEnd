package com.StoryCraftBackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.StoryCraftBackend.entity.Role;
import com.StoryCraftBackend.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/createRole")
    public Role createRole(@RequestBody Role role) {
        return roleService.createNewRole(role);
    }

    @PutMapping("/{roleName}")
    public Role updateRoleDescription(@PathVariable String roleName, @RequestBody Role newRoleData) {
        return roleService.updateRoleDescription(roleName, newRoleData);
    }

    @DeleteMapping("/{roleName}")
    public void deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{roleName}")
    public Role getRoleByName(@PathVariable String roleName) {
        return roleService.getRoleByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
    }

}
