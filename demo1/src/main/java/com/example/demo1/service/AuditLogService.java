package com.example.demo1.service;

import com.example.demo1.entity.AuditLog;
import com.example.demo1.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // ✅ Save audit log
    public void log(String userType, String username,
                    String action, String tableName,
                    String description) {

        AuditLog audit = new AuditLog();
        audit.setUserType(userType);
        audit.setUsername(username);
        audit.setAction(action);
        audit.setTableName(tableName);
        audit.setDescription(description);

        auditLogRepository.save(audit);
        System.out.println("✅ Audit logged: " + action + " by " + username);
    }

    // ✅ Get All
    public List<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }

    // ✅ Get by username
    public List<AuditLog> getByUsername(String username) {
        return auditLogRepository.findByUsername(username);
    }

    // ✅ Get by action
    public List<AuditLog> getByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    // ✅ Get by userType
    public List<AuditLog> getByUserType(String userType) {
        return auditLogRepository.findByUserType(userType);
    }

    // ✅ Get by tableName
    public List<AuditLog> getByTableName(String tableName) {
        return auditLogRepository.findByTableName(tableName);
    }

    // ✅ Search + pagination
    public Page<AuditLog> search(String keyword, int page,
                                 int size, String sortBy,
                                 String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return auditLogRepository
                .findByUsernameContainingIgnoreCase(keyword, pageable);
    }

}