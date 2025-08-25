package com.example.demo.controller;

import com.example.demo.service.TwilioWhatsappService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private TwilioWhatsappService whatsappService;

    @PostMapping("/place")
    public String placeOrder(
            @RequestParam String phoneNumber,
            @RequestParam String orderDetails) {

        // Optionally: save order in DB here

        // Send WhatsApp message
        String result = whatsappService.sendWhatsappMessage(phoneNumber, "Your order details: " + orderDetails);

        return "Order placed. " + result;
    }
}
