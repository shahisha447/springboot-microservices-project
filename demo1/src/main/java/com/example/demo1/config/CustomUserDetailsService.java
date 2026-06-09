package com.example.demo1.config;

import com.example.demo1.entity.Manager;
import com.example.demo1.entity.Product;
import com.example.demo1.repository.ManagerRepository;
import com.example.demo1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private ManagerRepository managerRepository;
    @Autowired private ProductRepository productRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. First check Manager table
        var managerOpt = managerRepository.findByUsername(username);
        if (managerOpt.isPresent()) {
            Manager manager = managerOpt.get();
            return new User(
                    manager.getUsername(),
                    manager.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_MANAGER"))
            );
        }

        // 2. Then check Product table
        var productOpt = productRepository.findByUsername(username);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            return new User(
                    product.getUsername(),
                    product.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_PRODUCT"))
            );
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}