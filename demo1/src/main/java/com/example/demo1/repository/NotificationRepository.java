package com.example.demo1.repository;

import com.example.demo1.entity.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    // ✅ Find by type
    List<Notification> findByType(String type);

    // ✅ Find unread
    List<Notification> findByIsRead(boolean isRead);
}