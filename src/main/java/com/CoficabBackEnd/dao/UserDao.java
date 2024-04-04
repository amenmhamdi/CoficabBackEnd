package com.CoficabBackEnd.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.CoficabBackEnd.entity.User;

@Repository
public interface UserDao extends CrudRepository<User, String> {
}
