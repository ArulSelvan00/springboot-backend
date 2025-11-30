package com.example.demo.controller;

import com.example.demo.model.UserOrder;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(
        origins = "https://endearing-heliotrope-12d102.netlify.app",
        methods = {RequestMethod.POST, RequestMethod.OPTIONS}
)
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Endpoint for customers to place an order after payment/checkout.
     * Maps to: POST /api/orders/place-shipping
     */
    @PostMapping("/place-shipping")
    public ResponseEntity<?> placeShippingOrder(@RequestBody UserOrder order) {
        if (order == null || order.getProductId() == null || order.getQuantity() == null || order.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Invalid or incomplete order data (missing Product ID or Quantity).");
        }

        try {
            UserOrder savedOrder = orderService.placeShippingOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
        } catch (RuntimeException e) {
            // Catch RuntimeException thrown by the service for business errors (stock/product not found)
            System.err.println("Order placement failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Order placement failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to place order due to server error: " + e.getMessage());
        }
    }

    // NOTE: Removed the redundant old /place endpoint.
}