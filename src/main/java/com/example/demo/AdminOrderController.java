package com.example.demo.controller;

import com.example.demo.model.UserOrder;
import com.example.demo.model.DeliveredOrder;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(origins = "http://localhost:3000") // Assuming React Admin is running locally
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    // GET /api/admin/orders (Active Orders List)
    @GetMapping
    public List<UserOrder> getAllActiveOrders() {
        return orderService.getAllOrders();
    }

    // POST /api/admin/orders/{id}/out-for-delivery
    @PostMapping("/{id}/out-for-delivery")
    public ResponseEntity<String> outForDelivery(@PathVariable Long id) {
        boolean ok = orderService.markOutForDelivery(id);
        if (ok) {
            return ResponseEntity.ok("Order marked as Out for Delivery");
        }
        return ResponseEntity.status(404).body("Order not found or already delivered.");
    }

    // POST /api/admin/orders/{id}/deliver
    @PostMapping("/{id}/deliver")
    public ResponseEntity<String> deliverOrder(@PathVariable Long id) {
        boolean ok = orderService.moveToDelivered(id);
        if (ok) {
            return ResponseEntity.ok("Order marked as Delivered");
        }
        return ResponseEntity.status(404).body("Order not found.");
    }

    // GET /api/admin/orders/delivered (Delivered History List)
    @GetMapping("/delivered")
    public List<DeliveredOrder> getDeliveredOrders() {
        return orderService.getDeliveredOrders();
    }
}