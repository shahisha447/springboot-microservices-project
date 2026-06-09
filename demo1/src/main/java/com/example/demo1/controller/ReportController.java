package com.example.demo1.controller;

import com.example.demo1.entity.Report;
import com.example.demo1.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired private ReportService reportService;

    // ✅ Generate report automatically
    @Operation(summary = "Generate Report", description = "Automatically generates report based on report type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid report type")
    })
    @PostMapping("/generate/{reportType}")
    public Report generate(@PathVariable String reportType) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return reportService.generateReport(reportType, auth.getName());
    }

    // ✅ Create manual report
    @Operation(summary = "Create Manual Report", description = "Creates a custom report manually")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid report data")
    })
    @PostMapping
    public Report create(@RequestBody Report report) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return reportService.create(report, auth.getName());
    }

    // ✅ Get All
    @Operation(summary = "Get All Reports", description = "Fetches all reports from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully")
    })
    @GetMapping
    public List<Report> getAll() {
        return reportService.getAll();
    }

    // ✅ Get by ID
    @Operation(summary = "Get Report By ID", description = "Fetch report details using report ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report found successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    @GetMapping("/{id}")
    public Report getById(@PathVariable Long id) {
        return reportService.getById(id);
    }

    // ✅ Get by Type
    @Operation(summary = "Get Reports By Type", description = "Fetches reports using report type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Report type not found")
    })
    @GetMapping("/type/{reportType}")
    public List<Report> getByType(@PathVariable String reportType) {
        return reportService.getByType(reportType);
    }

    // ✅ Delete
    @Operation(summary = "Delete Report", description = "Deletes report using report ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        reportService.delete(id, auth.getName());
        return "Report deleted!";
    }

    // ✅ Search
    @Operation(summary = "Search Reports", description = "Searches reports using keyword with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public Page<Report> search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return reportService.search(keyword, page, size, sortBy, direction);
    }

    // ✅ NEW — Date Filter
    @Operation(summary = "Filter Reports by Date", description = "Get reports between two dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports filtered successfully")
    })
    @GetMapping("/filter")
    public List<Report> getByDateRange(
            @RequestParam String from,
            @RequestParam String to) {
        return reportService.getByDateRange(from, to);
    }

    // ✅ NEW — PDF Download
    @Operation(summary = "Download Report as PDF", description = "Downloads report as PDF file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    @GetMapping("/download/pdf/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) throws Exception {
        byte[] pdf = reportService.generatePdf(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report_" + id + ".pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // ✅ NEW — Excel Export
    @Operation(summary = "Export Reports as Excel", description = "Exports all reports as Excel file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Excel exported successfully")
    })
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel() throws Exception {
        byte[] excel = reportService.generateExcel();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=reports.xlsx")
                .contentType(org.springframework.http.MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }
}