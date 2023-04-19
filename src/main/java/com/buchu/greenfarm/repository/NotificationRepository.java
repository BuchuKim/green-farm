package com.buchu.greenfarm.repository;

import com.buchu.greenfarm.entity.Notification;
import com.buchu.greenfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query("SELECT n FROM Notification n WHERE n.receivingUser = :receivingUser ORDER BY createdAt DESC LIMIT 30")
    public Optional<List<Notification>> findByReceivingUser(@Param("receivingUser") User receivingUser);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n NOT IN :notifications")
    public void deleteExceptFor(@Param("notifications") List<Long> notifications);

    @Modifying
    public void deleteByReceivingUser(User receivingUser);
}
