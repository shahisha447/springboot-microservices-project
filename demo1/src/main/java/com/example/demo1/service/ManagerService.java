package com.example.demo1.service;

import com.example.demo1.entity.Manager;
import com.example.demo1.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    @Autowired private ManagerRepository managerRepository;
    @Autowired private AuditLogService auditLogService;

    // ✅ Cache all managers
    @Cacheable(value = "managers", key = "'all'")
    public List<Manager> getAllManagers() {
        System.out.println("🗄️ Fetching managers from DATABASE...");
        return managerRepository.findAll();
    }

    // ✅ Cache single manager by id
    @Cacheable(value = "managers", key = "#id")
    public Manager getManagerById(Long id) {
        System.out.println("🗄️ Fetching manager " + id + " from DATABASE...");
        return managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
    }

    // ✅ Clear cache when updated
    @CacheEvict(value = "managers", allEntries = true)
    public Manager updateManager(Long id, Manager updated) {
        System.out.println("🗑️ Managers cache cleared after update!");
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        String oldUsername = manager.getUsername();
        manager.setUsername(updated.getUsername());
        manager.setEmail(updated.getEmail());
        Manager saved = managerRepository.save(manager);

        auditLogService.log(
                "MANAGER",
                saved.getUsername(),
                "UPDATE",
                "manager",
                "Manager updated username from: " + oldUsername
                        + " to: " + saved.getUsername()
                        + " email: " + saved.getEmail()
        );
        return saved;
    }

    // ✅ Clear cache when deleted
    @CacheEvict(value = "managers", allEntries = true)
    public void deleteManager(Long id) {
        System.out.println("🗑️ Managers cache cleared after delete!");
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        auditLogService.log(
                "MANAGER",
                manager.getUsername(),
                "DELETE",
                "manager",
                "Manager deleted: " + manager.getUsername()
        );
        managerRepository.deleteById(id);
    }

    // ✅ Cache search results
    @Cacheable(value = "managers",
            key = "#keyword + #page + #size + #sortBy + #direction")
    public Page<Manager> searchManagers(String keyword, int page,
                                        int size, String sortBy,
                                        String direction) {
        System.out.println("🗄️ Searching managers from DATABASE...");
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return managerRepository
                .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, pageable);
    }

    // ✅ Cache pagination
    @Cacheable(value = "managers", key = "'page_' + #pageable.pageNumber")
    public Page<Manager> getManagers(Pageable pageable) {
        System.out.println("🗄️ Fetching managers page from DATABASE...");
        return managerRepository.findAll(pageable);
    }
}