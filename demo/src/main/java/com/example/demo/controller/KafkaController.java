package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.kafka.KafkaProducer;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/kafka")
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
public class KafkaController {

    @Autowired private KafkaProducer kafkaProducer;
    @Autowired private UserService userService;

    @PostMapping("/send-users")
    public String sendAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(kafkaProducer::sendUser);
        return "Sent " + users.size() + " users to Kafka!";
    }

    @PostMapping("/send-user/{id}")
    public String sendUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        kafkaProducer.sendUser(user);
        return "Sent user: " + user.getUsername();
    }
}