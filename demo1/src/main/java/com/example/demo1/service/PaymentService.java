package com.example.demo1.service;

import com.example.demo1.entity.Payment;
import com.example.demo1.repository.PaymentRepository;
import com.example.demo1.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired private PaymentRepository paymentRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private AuditLogService auditLogService;
    @Autowired
    private NotificationService notificationService;

    // ✅ Create Payment
    public Payment createPayment(Long orderId, Payment payment, String username) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        payment.setOrder(order);
        payment.setPaidBy(username);
        Payment saved = paymentRepository.save(payment);
        notificationService.create(
                "Payment Successful",
                "PAYMENT");

        auditLogService.log("MANAGER", username, "CREATE", "payment",
                "Payment created for order: " + orderId);
        return saved;
    }

    // ✅ Get All
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    // ✅ Get by ID
    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found!"));
    }

    // ✅ Get by Order
    public List<Payment> getByOrder(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    // ✅ Get by PaidBy
    public List<Payment> getByPaidBy(String paidBy) {
        return paymentRepository.findByPaidBy(paidBy);
    }

    // ✅ Delete
    public void delete(Long id, String username) {
        paymentRepository.deleteById(id);
        auditLogService.log("MANAGER", username, "DELETE", "payment",
                "Payment deleted: " + id);
    }

    // ✅ Search
    public Page<Payment> search(String keyword, int page, int size,
                                String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return paymentRepository.search(keyword, pageable);
    }
}