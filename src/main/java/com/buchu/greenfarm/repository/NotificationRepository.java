package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.Notification;
import com.buchu.greenfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    public Optional<List<Notification>> findByReceivingUser(User receivingUser);
}
