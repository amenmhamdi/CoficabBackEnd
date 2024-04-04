package com.CoficabBackEnd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CoficabBackEnd.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Custom query method to find notifications by sender username
    List<Notification> findBySender_UserName(String userName);

    // Custom query method to find notifications by receiver username
    List<Notification> findByReceiver_UserName(String userName);
}
