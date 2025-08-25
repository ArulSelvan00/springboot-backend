package com.example.demo;

import com.example.demo.model.UserOrder;
import com.example.demo.UserOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderPlacementController {

    @Autowired
    private UserOrderRepository orderRepository;

    @PostMapping("/place-shipping")
    public ResponseEntity<String> placeOrder(@RequestBody UserOrder order) {
        try {
            if (order == null) {
                return ResponseEntity.badRequest().body("Invalid order data.");
            }

            // Example validation - you can add more
            if (order.getCustomerName() == null || order.getCustomerName().isEmpty()) {
                return ResponseEntity.badRequest().body("Customer name is required.");
            }

            orderRepository.save(order);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("✅ Order placed and saved to DB successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to place order: " + e.getMessage());
        }
    }
}
