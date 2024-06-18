package com.IsetPortalBackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.IsetPortalBackend.entity.User;
import com.IsetPortalBackend.entity.UserImage;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUser(User user);
    List<UserImage> findByUser_Id(Long userId);
}
