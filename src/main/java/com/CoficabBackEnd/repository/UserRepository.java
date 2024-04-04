package com.CoficabBackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CoficabBackEnd.entity.User;

public interface UserRepository extends JpaRepository<User, String>{

	public User findByUserName (String userName);
	List<User> findByUserNameIn(List<String> userNames);
	List<User> findByEmail(String email);
	public User findByResetToken(String resetToken);
}
