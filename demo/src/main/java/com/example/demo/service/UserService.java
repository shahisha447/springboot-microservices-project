package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.kafka.KafkaProducer;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private KafkaProducer kafkaProducer;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        User saved = userRepository.save(user);

        Map<String, Object> data = new HashMap<>();
        data.put("action", "CREATE"); // ✅
        data.put("id", saved.getId());
        data.put("username", saved.getUsername());
        data.put("email", saved.getEmail());
        data.put("role", saved.getRole());
        data.put("password", saved.getPassword());
        kafkaProducer.sendUser(data);

        return saved;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long id, User updatedUser) {

        User user = getUserById(id);

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null &&
                !updatedUser.getPassword().isEmpty()) {

            user.setPassword(
                    passwordEncoder.encode(
                            updatedUser.getPassword()
                    )
            );
        }

        User saved = userRepository.save(user);

        Map<String, Object> data = new HashMap<>();

        data.put("action", "UPDATE");
        data.put("id", saved.getId());
        data.put("username", saved.getUsername());
        data.put("email", saved.getEmail());
        data.put("role", saved.getRole());

        kafkaProducer.sendUser(data);

        return saved;
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);

        // ✅ Send DELETE to Kafka BEFORE deleting
        Map<String, Object> data = new HashMap<>();
        data.put("action", "DELETE");
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("email", user.getEmail());
        data.put("role", user.getRole());
        kafkaProducer.sendUser(data);

        userRepository.deleteById(id);
    }

    public Page<User> searchUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByUsernameContainingIgnoreCase(keyword, pageable);
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}