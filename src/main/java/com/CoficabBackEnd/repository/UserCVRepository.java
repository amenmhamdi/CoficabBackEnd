package com.CoficabBackEnd.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.CoficabBackEnd.entity.User;
import com.CoficabBackEnd.entity.UserCV;

@Repository
public interface UserCVRepository extends JpaRepository<UserCV, Long> {
    // Method to fetch CVs by username
    List<UserCV> findByUserUserName(String username);

    UserCV findByUser(User user);
    
    @Query("SELECT cv, u.userName FROM UserCV cv JOIN cv.user u")
    List<Object[]> findAllWithUsername();


}
