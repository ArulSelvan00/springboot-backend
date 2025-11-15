package com.example.demo.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioWhatsappService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    public String sendWhatsappMessage(String to, String message) {
        try {
            Twilio.init(accountSid, authToken);

            // Format number for WhatsApp API
            String formatted = to.startsWith("whatsapp:") ? to : "whatsapp:" + to;

            Message.creator(
                    new com.twilio.type.PhoneNumber(formatted),
                    new com.twilio.type.PhoneNumber(fromNumber),
                    message
            ).create();

            return "✅ WhatsApp message sent to " + to;
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to send WhatsApp message: " + e.getMessage();
        }
    }
}
