package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.kafka.KafkaProducer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    @Autowired private CustomerRepository customerRepository;
    @Autowired private KafkaProducer kafkaProducer;

    public Customer createCustomer(Customer customer) {
        Customer saved = customerRepository.save(customer);

        Map<String, Object> data = new HashMap<>();
        data.put("action", "CREATE"); // ✅
        data.put("id", saved.getId());
        data.put("name", saved.getName());
        data.put("email", saved.getEmail());
        data.put("phone", saved.getPhone());
        data.put("address", saved.getAddress());
        data.put("username", saved.getUsername());
        data.put("role", saved.getRole());
        kafkaProducer.sendCustomer(data);

        return saved;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer updateCustomer(Long id, Customer updated) {
        Customer customer = getCustomerById(id);
        customer.setName(updated.getName());
        customer.setEmail(updated.getEmail());
        customer.setPhone(updated.getPhone());
        customer.setAddress(updated.getAddress());
        Customer saved = customerRepository.save(customer);

        // ✅ Send UPDATE to Kafka
        Map<String, Object> data = new HashMap<>();
        data.put("action", "UPDATE");
        data.put("id", saved.getId());
        data.put("name", saved.getName());
        data.put("email", saved.getEmail());
        data.put("phone", saved.getPhone());
        data.put("address", saved.getAddress());
        data.put("username", saved.getUsername());
        data.put("role", saved.getRole());
        kafkaProducer.sendCustomer(data);

        return saved;
    }

    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);

        // ✅ Send DELETE to Kafka BEFORE deleting
        Map<String, Object> data = new HashMap<>();
        data.put("action", "DELETE");
        data.put("id", customer.getId());
        data.put("name", customer.getName());
        data.put("username", customer.getUsername());
        data.put("email", customer.getEmail());
        data.put("role", customer.getRole());
        kafkaProducer.sendCustomer(data);

        customerRepository.deleteById(id);
    }

    public Page<Customer> searchCustomers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public Page<Customer> getCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
}