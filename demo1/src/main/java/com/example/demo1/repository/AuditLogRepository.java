package com.example.demo1.repository;

import com.example.demo1.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // ✅ Find by username
    List<AuditLog> findByUsername(String username);

    // ✅ Find by action
    List<AuditLog> findByAction(String action);

    // ✅ Find by userType
    List<AuditLog> findByUserType(String userType);

    // ✅ Find by tableName
    List<AuditLog> findByTableName(String tableName);

    // ✅ Search + pagination
    Page<AuditLog> findByUsernameContainingIgnoreCase(
            String keyword, Pageable pageable);
}