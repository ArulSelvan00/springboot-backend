package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.model.UserOrder;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserOrderRepository;
import com.example.demo.service.TwilioWhatsappService; // Assumed service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional; // 🎯 CORRECTED: Use Spring's Transactional
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired private UserOrderRepository userOrderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private TwilioWhatsappService whatsappService;

    // --- 1. Endpoint to Save Order Details to DB & Update Stock ---
    // Maps to: POST /api/orders/place-shipping
    @PostMapping("/place-shipping")
    @Transactional // Ensures stock update and order save succeed or fail together
    public ResponseEntity<?> placeShippingOrder(@RequestBody UserOrder order) {
        try {
            if (order == null || order.getProductId() == null || order.getQuantity() == null || order.getQuantity() <= 0) {
                return ResponseEntity.badRequest().body("Invalid or incomplete order data (missing Product ID or Quantity).");
            }

            Long productId = order.getProductId();
            Integer orderedQuantity = order.getQuantity();

            // 1. Find Product for Stock Update using ID (More reliable than name)
            Optional<Product> productOpt = productRepository.findById(productId);

            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                int currentStock = product.getStock();

                // 2. Validate and Update Stock Level
                if (currentStock >= orderedQuantity) {
                    product.setStock(currentStock - orderedQuantity);
                    // The save ensures the stock is decremented within the transaction
                    productRepository.save(product);

                    System.out.println("✅ Stock updated for " + product.getName() + ". New stock: " + product.getStock());
                } else {
                    // Fail the transaction if stock is insufficient
                    System.err.println("Stock update failed: Insufficient stock for Product ID " + productId);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Stock update failed: Insufficient stock for " + product.getName() + ". Available: " + currentStock);
                }
            } else {
                System.err.println("Product not found by ID: " + productId + ". Cannot update stock.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product not found for stock update.");
            }

            // 3. Save Order Record (Uses the decrement from the same transaction)
            UserOrder savedOrder = userOrderRepository.save(order);

            // Return the saved order object to the frontend for confirmation
            return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to place order: " + e.getMessage());
        }
    }

    // --- 2. Endpoint to Send WhatsApp Message (No changes needed) ---
    @PostMapping("/place")
    public String placeOrder(
            @RequestParam String phoneNumber,
            @RequestParam String orderDetails) {

        try {
            // Logic for formatting phone and sending message (assumed correct from previous steps)
            String phone = phoneNumber.trim();
            if (!phone.startsWith("+")) {
                if (phone.length() == 10) {
                    phone = "+91" + phone;
                } else {
                    phone = "+" + phone;
                }
            }
            String msg = "🛒 *Order Confirmation!*...\n" + orderDetails; // Simplified message content

            whatsappService.sendWhatsappMessage(phone, msg);

            return "Order placed. WhatsApp message sent.";

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to send WhatsApp order confirmation: " + e.getMessage();
        }
    }
}