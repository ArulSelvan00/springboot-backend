package com.example.demo.controller;

import com.example.demo.model.UserOrder;
import com.example.demo.UserOrderRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // Allow React
@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final UserOrderRepository orderRepository;

    public AdminOrderController(UserOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public List<UserOrder> getAllOrders() {
        return orderRepository.findAll();
    }
}
