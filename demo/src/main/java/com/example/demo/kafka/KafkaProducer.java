package com.example.demo.kafka;

import com.example.demo.entity.Customer;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Async  // ✅ runs in background thread — won't block request!
    public void sendUser(Map<String, Object> data) {
        try {
            kafkaTemplate.send("user-to-userinfo", data);
            System.out.println("✅ User message sent to Kafka");
        } catch (Exception e) {
            System.out.println("⚠️ Kafka unavailable: " + e.getMessage());
        }
    }

    @Async  // ✅ runs in background thread — won't block request!
    public void sendCustomer(Map<String, Object> data) {
        try {
            kafkaTemplate.send("customer-to-customerinfo", data);
            System.out.println("✅ Customer message sent to Kafka");
        } catch (Exception e) {
            System.out.println("⚠️ Kafka unavailable: " + e.getMessage());
        }
    }

    @Async
    public void sendCustomer(Customer customer) {
        try {
            Map<String, Object> data = Map.of(
                    "name", customer.getName(),
                    "email", customer.getEmail(),
                    "phone", customer.getPhone(),
                    "address", customer.getAddress(),
                    "username", customer.getUsername(),
                    "password", customer.getPassword(),
                    "role", customer.getRole()
            );
            kafkaTemplate.send("customer-to-customerinfo", data);
            System.out.println("✅ Customer message sent to Kafka");
        } catch (Exception e) {
            System.out.println("⚠️ Kafka unavailable: " + e.getMessage());
        }
    }


    @Async
    public void sendUser(User user) {
    }
}