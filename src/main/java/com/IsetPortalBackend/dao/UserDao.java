package com.IsetPortalBackend.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.IsetPortalBackend.entity.User;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    Boolean existsByEmail(String email);
}
