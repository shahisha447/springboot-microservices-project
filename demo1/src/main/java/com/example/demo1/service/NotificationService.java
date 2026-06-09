package com.example.demo1.service;

import com.example.demo1.entity.Notification;
import com.example.demo1.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // ✅ Create
    public Notification create(
            String message,
            String type) {

        Notification notification =
                new Notification(
                        message,
                        type
                );

        return notificationRepository
                .save(notification);
    }

    // ✅ Get All
    public List<Notification> getAll() {

        return notificationRepository
                .findAll();
    }

    // ✅ Get By ID
    public Notification getById(Long id) {

        return notificationRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Notification not found"));
    }

    // ✅ Get By Type
    public List<Notification> getByType(
            String type) {

        return notificationRepository
                .findByType(type);
    }

    // ✅ Get Unread
    public List<Notification> getUnread() {

        return notificationRepository
                .findByIsRead(false);
    }

    // ✅ Mark As Read
    public Notification markAsRead(
            Long id) {

        Notification notification =
                getById(id);

        notification.setRead(true);

        return notificationRepository
                .save(notification);
    }

    // ✅ Delete
    public void delete(Long id) {

        notificationRepository
                .deleteById(id);
    }
}