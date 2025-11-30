package com.example.demo.controller;

import com.example.demo.model.UserOrder;
import com.example.demo.model.DeliveredOrder;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@CrossOrigin(
        origins = "https://endearing-heliotrope-12d102.netlify.app",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
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
    public String outForDelivery(@PathVariable Long id) {
        boolean ok = orderService.markOutForDelivery(id);
        return ok ? "Order marked as Out for Delivery" : "Order not found";
    }

    // POST /api/admin/orders/{id}/deliver
    @PostMapping("/{id}/deliver")
    public String deliverOrder(@PathVariable Long id) {
        boolean ok = orderService.moveToDelivered(id);
        return ok ? "Order marked as Delivered" : "Order not found";
    }

    // GET /api/admin/orders/delivered (Delivered History List)
    @GetMapping("/delivered")
    public List<DeliveredOrder> getDeliveredOrders() {
        return orderService.getDeliveredOrders();
    }
}