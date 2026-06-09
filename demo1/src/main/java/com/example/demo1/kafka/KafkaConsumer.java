package com.example.demo1.kafka;

import com.example.demo1.entity.CustomerInfo;
import com.example.demo1.entity.UserInfo;
import com.example.demo1.repository.CustomerInfoRepository;
import com.example.demo1.repository.UserInfoRepository;
import com.example.demo1.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaConsumer {

    @Autowired private UserInfoRepository userInfoRepository;
    @Autowired private CustomerInfoRepository customerInfoRepository;
    @Autowired private AuditLogService auditLogService;

    // ✅ USER CONSUMER
    @KafkaListener(topics = "user-to-userinfo", groupId = "demo1-group-v2")
    public void consumeUser(Map<String, Object> data) {
        System.out.println("MESSAGE RECEIVED USER: " + data);
        String username = (String) data.get("username");
        String action = (String) data.get("action");

        if ("CREATE".equals(action)) {
            if (userInfoRepository.existsByUsername(username)) {
                System.out.println("⚠️ User already exists");
                return;
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setId(Long.valueOf(data.get("id").toString()));
            userInfo.setUsername(username);
            userInfo.setEmail((String) data.get("email"));
            userInfo.setRole((String) data.get("role"));
            userInfo.setPassword((String) data.get("password"));
            userInfoRepository.save(userInfo);
            auditLogService.log("USER", username, "CREATE", "user_info",
                    "User created with email: " + data.get("email"));
            System.out.println("✅ User created in user_info");

        } else if ("UPDATE".equals(action)) {
            userInfoRepository.findById(Long.valueOf(data.get("id").toString()))
                    .ifPresent(userInfo -> {
                        String oldEmail = userInfo.getEmail();
                        userInfo.setUsername(username);
                        userInfo.setEmail((String) data.get("email"));
                        userInfo.setRole((String) data.get("role"));
                        userInfoRepository.save(userInfo);
                        auditLogService.log("USER", username, "UPDATE", "user_info",
                                "Email updated from: " + oldEmail + " to: " + data.get("email"));
                        System.out.println("✅ User updated in user_info");
                    });

        } else if ("DELETE".equals(action)) {
            userInfoRepository.findById(Long.valueOf(data.get("id").toString()))
                    .ifPresent(userInfo -> {
                        userInfoRepository.delete(userInfo);
                        auditLogService.log("USER", username, "DELETE", "user_info",
                                "User deleted: " + username);
                        System.out.println("✅ User deleted from user_info");
                    });
        }
    }

    // ✅ CUSTOMER CONSUMER
    @KafkaListener(topics = "customer-to-customerinfo", groupId = "demo1-group-v2")
    public void consumeCustomer(Map<String, Object> data) {
        System.out.println("MESSAGE RECEIVED CUSTOMER: " + data);
        String username = (String) data.get("username");
        String name = (String) data.get("name");
        String action = (String) data.get("action");

        if ("CREATE".equals(action)) {
            if (customerInfoRepository.existsByName(name)) {
                System.out.println("⚠️ Customer already exists");
                return;
            }
            CustomerInfo customerInfo = new CustomerInfo();
            customerInfo.setId(Long.valueOf(data.get("id").toString()));
            customerInfo.setName(name);
            customerInfo.setEmail((String) data.get("email"));
            customerInfo.setPhone((String) data.get("phone"));
            customerInfo.setAddress((String) data.get("address"));
            customerInfo.setUsername(username);
            customerInfo.setRole((String) data.get("role"));
            customerInfo.setPassword((String) data.get("password"));
            customerInfoRepository.save(customerInfo);
            auditLogService.log("CUSTOMER", username, "CREATE", "customer_info",
                    "Customer created: " + name);
            System.out.println("✅ Customer created in customer_info");

        } else if ("UPDATE".equals(action)) {
            customerInfoRepository.findById(Long.valueOf(data.get("id").toString()))
                    .ifPresent(customerInfo -> {
                        String oldName = customerInfo.getName();
                        customerInfo.setName(name);
                        customerInfo.setEmail((String) data.get("email"));
                        customerInfo.setPhone((String) data.get("phone"));
                        customerInfo.setAddress((String) data.get("address"));
                        customerInfoRepository.save(customerInfo);
                        auditLogService.log("CUSTOMER", username, "UPDATE", "customer_info",
                                "Name updated from: " + oldName + " to: " + name);
                        System.out.println("✅ Customer updated in customer_info");
                    });

        } else if ("DELETE".equals(action)) {
            customerInfoRepository.findById(Long.valueOf(data.get("id").toString()))
                    .ifPresent(customerInfo -> {
                        customerInfoRepository.delete(customerInfo);
                        auditLogService.log("CUSTOMER", username, "DELETE", "customer_info",
                                "Customer deleted: " + username);
                        System.out.println("✅ Customer deleted from customer_info");
                    });
        }
    }
}