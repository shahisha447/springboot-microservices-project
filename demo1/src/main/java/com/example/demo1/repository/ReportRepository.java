package com.example.demo1.repository;

import com.example.demo1.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReportType(String reportType);

    @Query("SELECT r FROM Report r WHERE r.title LIKE %:keyword% OR r.reportType LIKE %:keyword%")
    Page<Report> search(@Param("keyword") String keyword, Pageable pageable);

    // ← ADD this for date filter
    List<Report> findByGeneratedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Report> findTop5ByOrderByIdDesc();
}