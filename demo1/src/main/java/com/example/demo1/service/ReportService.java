package com.example.demo1.service;

import com.example.demo1.entity.Report;
import com.example.demo1.repository.*;
import com.itextpdf.text.pdf.*;
// PDF imports
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

// Excel imports
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    @Autowired private ReportRepository reportRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AuditLogService auditLogService;
    @Autowired
    private NotificationService notificationService;

    // ✅ Generate Report
    public Report generateReport(String reportType, String username) {
        Report report = new Report();
        report.setReportType(reportType);
        report.setGeneratedBy(username);

        switch (reportType.toUpperCase()) {
            case "SALES":
                report.setTitle("Sales Report");
                report.setTotalOrders((int) orderRepository.count());
                report.setTotalProducts((int) productRepository.count());
                report.setTotalAmount(
                        paymentRepository.findAll()
                                .stream()
                                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0)
                                .sum()
                );
                report.setDescription("Total sales summary report");
                break;

            case "PAYMENT":
                report.setTitle("Payment Report");
                var payments = paymentRepository.findAll();
                report.setTotalOrders(payments.size());
                report.setTotalAmount(
                        payments.stream()
                                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0)
                                .sum()
                );
                report.setDescription("Payment summary report");
                break;

            case "ORDER":
                report.setTitle("Order Report");
                report.setTotalOrders((int) orderRepository.count());
                report.setDescription("Order summary report");
                break;

            case "PRODUCT":
                report.setTitle("Product Report");
                report.setTotalProducts((int) productRepository.count());
                report.setDescription("Product summary report");
                break;

            default:
                report.setTitle("General Report");
                report.setDescription("General summary");
        }

        Report saved = reportRepository.save(report);
        notificationService.create(
                "New Report Generated",
                "REPORT");
        auditLogService.log("MANAGER", username, "CREATE", "report",
                "Report generated: " + reportType);
        return saved;
    }

    // ✅ Create manual report
    public Report create(Report report, String username) {
        report.setGeneratedBy(username);
        Report saved = reportRepository.save(report);
        auditLogService.log("MANAGER", username, "CREATE", "report",
                "Manual report created: " + report.getTitle());
        return saved;
    }

    // ✅ Get All
    public List<Report> getAll() {
        return reportRepository.findAll();
    }

    // ✅ Get by ID
    public Report getById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found!"));
    }

    // ✅ Get by Type
    public List<Report> getByType(String reportType) {
        return reportRepository.findByReportType(reportType);
    }

    // ✅ Delete
    public void delete(Long id, String username) {
        reportRepository.deleteById(id);
        auditLogService.log("MANAGER", username, "DELETE", "report",
                "Report deleted: " + id);
    }

    // ✅ Search
    public Page<Report> search(String keyword, int page, int size,
                               String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return reportRepository.search(keyword, pageable);
    }

    // ✅ NEW — Date Filter
    public List<Report> getByDateRange(String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime fromDate = LocalDateTime.parse(from + "T00:00:00", formatter);
        LocalDateTime toDate = LocalDateTime.parse(to + "T23:59:59", formatter);
        return reportRepository.findByGeneratedAtBetween(fromDate, toDate);
    }

    // ✅ NEW — PDF Download
    public byte[] generatePdf(Long id) throws Exception {
        Report report = getById(id);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 12);

        document.add(new Paragraph(report.getTitle(), titleFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Type: " + report.getReportType(), normalFont));
        document.add(new Paragraph("Description: " + report.getDescription(), normalFont));
        document.add(new Paragraph("Total Amount: " + report.getTotalAmount(), normalFont));
        document.add(new Paragraph("Total Orders: " + report.getTotalOrders(), normalFont));
        document.add(new Paragraph("Total Products: " + report.getTotalProducts(), normalFont));
        document.add(new Paragraph("Generated By: " + report.getGeneratedBy(), normalFont));
        document.add(new Paragraph("Generated At: " + report.getGeneratedAt(), normalFont));

        document.close();
        return out.toByteArray();
    }

    // ✅ NEW — Excel Export
    public byte[] generateExcel() throws Exception {
        List<Report> reports = reportRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reports");

        // Header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Type");
        header.createCell(2).setCellValue("Title");
        header.createCell(3).setCellValue("Total Amount");
        header.createCell(4).setCellValue("Total Orders");
        header.createCell(5).setCellValue("Total Products");
        header.createCell(6).setCellValue("Generated By");
        header.createCell(7).setCellValue("Generated At");

        // Data rows
        int rowNum = 1;
        for (Report report : reports) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(report.getId());
            row.createCell(1).setCellValue(report.getReportType());
            row.createCell(2).setCellValue(report.getTitle());
            row.createCell(3).setCellValue(report.getTotalAmount() != null ? report.getTotalAmount() : 0);
            row.createCell(4).setCellValue(report.getTotalOrders() != null ? report.getTotalOrders() : 0);
            row.createCell(5).setCellValue(report.getTotalProducts() != null ? report.getTotalProducts() : 0);
            row.createCell(6).setCellValue(report.getGeneratedBy());
            row.createCell(7).setCellValue(report.getGeneratedAt() != null ? report.getGeneratedAt().toString() : "");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }
}