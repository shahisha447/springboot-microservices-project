package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.entity.User;
import com.example.demo.kafka.KafkaProducer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DataSyncService {

    @Autowired private KafkaProducer kafkaProducer;
    @Autowired private UserRepository userRepository;
    @Autowired private CustomerRepository customerRepository;

    // ✅ NO @PostConstruct — only runs when called manually via API
    public void syncAllData() {
        List<User> users = userRepository.findAll();
        List<Customer> customers = customerRepository.findAll();
        users.forEach(kafkaProducer::sendUser);
        customers.forEach(kafkaProducer::sendCustomer);
        System.out.println("✅ Synced " + users.size() + " users and " + customers.size() + " customers!");
    }
}