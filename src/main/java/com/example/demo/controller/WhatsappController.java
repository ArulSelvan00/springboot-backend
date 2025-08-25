package com.example.demo.controller;

import com.example.demo.dto.WhatsappRequest;
import com.example.demo.service.TwilioWhatsappService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
@CrossOrigin(origins = "*")
public class WhatsappController {

    @Autowired
    private TwilioWhatsappService whatsappService;

    @PostMapping("/send")
    public String sendMessage(@RequestBody WhatsappRequest request) {
        return whatsappService.sendWhatsappMessage(request.getTo(), request.getMessage());
    }
}
