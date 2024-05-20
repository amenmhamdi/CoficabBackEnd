package com.StoryCraftBackend.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.StoryCraftBackend.entity.User;

@Repository
public interface UserDao extends CrudRepository<User, String> {
}
