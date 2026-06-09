package com.example.demo1.service;

import com.example.demo1.entity.*;
import com.example.demo1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ManagerRepository managerRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AuditLogService auditLogService;
    @Autowired private NotificationService notificationService;

    // ✅ Create Order — clears cache
    @CacheEvict(value = "orders", allEntries = true)
    public Order createOrder(Long managerId, Long productId,
                             String createdBy) {
        System.out.println("🗑️ Orders cache cleared after new order!");

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Order order = new Order();
        order.setManager(manager);
        order.setProduct(product);
        order.setCreatedBy(createdBy);

        Order saved = orderRepository.save(order);

        notificationService.create("New Order Created", "ORDER");

        auditLogService.log(
                "MANAGER", createdBy, "CREATE", "orders",
                "Order created with manager: " + manager.getUsername()
                        + " product: " + product.getName()
        );

        return saved;
    }

    // ✅ Get All — cached
    @Cacheable(value = "orders", key = "'all'")
    public List<Order> getAllOrders() {
        System.out.println("🗄️ Fetching orders from DATABASE...");
        return orderRepository.findAll();
    }

    // ✅ Get by ID — cached
    @Cacheable(value = "orders", key = "#id")
    public Order getOrderById(Long id) {
        System.out.println("🗄️ Fetching order " + id + " from DATABASE...");
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ Delete — clears cache
    @CacheEvict(value = "orders", allEntries = true)
    public void deleteOrder(Long id) {
        System.out.println("🗑️ Orders cache cleared after delete!");
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        auditLogService.log(
                "MANAGER", order.getCreatedBy(), "DELETE", "orders",
                "Order deleted with id: " + id
        );
        orderRepository.deleteById(id);
    }

    // ✅ Get by Manager — cached
    @Cacheable(value = "orders", key = "'manager_' + #managerId")
    public List<Order> getOrdersByManager(Long managerId) {
        System.out.println("🗄️ Fetching orders by manager from DATABASE...");
        return orderRepository.findByManager_Id(managerId);
    }

    // ✅ Get by Product — cached
    @Cacheable(value = "orders", key = "'product_' + #productId")
    public List<Order> getOrdersByProduct(Long productId) {
        System.out.println("🗄️ Fetching orders by product from DATABASE...");
        return orderRepository.findByProduct_Id(productId);
    }

    // ✅ Search — cached
    @Cacheable(value = "orders",
            key = "#keyword + '_' + #page + '_' + #size + '_' + #sortBy + '_' + #direction")
    public Page<Order> searchOrders(String keyword, int page,
                                    int size, String sortBy,
                                    String direction) {
        System.out.println("🗄️ Searching orders from DATABASE...");
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderRepository
                .findByCreatedByContainingIgnoreCase(keyword, pageable);
    }

    // ✅ Pagination — cached
    @Cacheable(value = "orders", key = "'page_' + #pageable.pageNumber")
    public Page<Order> getOrders(Pageable pageable) {
        System.out.println("🗄️ Fetching orders page from DATABASE...");
        return orderRepository.findAll(pageable);
    }
}