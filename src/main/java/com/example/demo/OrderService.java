package com.example.demo.service;

import com.example.demo.model.UserOrder;
import com.example.demo.model.DeliveredOrder;
import com.example.demo.model.Product;
import com.example.demo.repository.UserOrderRepository;
import com.example.demo.repository.DeliveredOrderRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class OrderService {

    // Inject Repositories and external Services
    @Autowired private UserOrderRepository userOrderRepository;
    @Autowired private DeliveredOrderRepository deliveredOrderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ReportService reportService;
    @Autowired private TwilioWhatsappService whatsappService;

    // ----------------------------------------------------
    // CUSTOMER FACING LOGIC (Place Order & Stock Update)
    // ----------------------------------------------------

    @Transactional
    public UserOrder placeShippingOrder(UserOrder order) {
        Long productId = order.getProductId();
        Integer orderedQuantity = order.getQuantity();

        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            int currentStock = product.getStock();

            if (currentStock >= orderedQuantity) {
                product.setStock(currentStock - orderedQuantity);
                productRepository.save(product);
            } else {
                throw new RuntimeException("Stock update failed: Insufficient stock for " + product.getName() + ". Available: " + currentStock);
            }
        } else {
            throw new RuntimeException("Product not found for stock update.");
        }
        return userOrderRepository.save(order);
    }

    // ----------------------------------------------------
    // ADMIN STATUS & HISTORY LOGIC
    // ----------------------------------------------------

    public List<UserOrder> getAllOrders() {
        return userOrderRepository.findAll();
    }

    @Transactional
    public boolean markOutForDelivery(Long id) {
        Optional<UserOrder> orderOpt = userOrderRepository.findById(id);
        if (orderOpt.isEmpty()) return false;
        UserOrder order = orderOpt.get();
        order.setStatus("out_for_delivery");
        userOrderRepository.save(order);

        // Send WhatsApp message (Assumed full content logic exists elsewhere)
        String phone = formatPhone(order.getContact());
        if (phone != null) {
            String msg = "📦 *Order Out for Delivery!*... (Full message content here)";
            whatsappService.sendWhatsappMessage(phone, msg);
        }

        return true;
    }

    public List<DeliveredOrder> getDeliveredOrders() {
        return deliveredOrderRepository.findAll();
    }

    @Transactional
    public boolean moveToDelivered(Long id) {
        Optional<UserOrder> orderOpt = userOrderRepository.findById(id);
        if (orderOpt.isEmpty()) return false;
        UserOrder order = orderOpt.get();

        // 1. Create DeliveredOrder and copy all necessary data (Atomic operation)
        DeliveredOrder delivered = new DeliveredOrder();
        delivered.setOrderId(order.getId());
        delivered.setProductName(order.getProductName());
        delivered.setCustomerName(order.getCustomerName());
        delivered.setStatus("delivered");
        delivered.setTotalPrice(order.getTotalPrice());
        delivered.setContact(order.getContact());
        delivered.setAddress(order.getAddress());
        delivered.setVillage(order.getVillage());
        delivered.setPincode(order.getPincode());
        delivered.setDeliveredDate(new Date());

        deliveredOrderRepository.save(delivered);

        // 2. Remove from pending list (Part of the same transaction)
        userOrderRepository.deleteById(id);

        // 3. Send WhatsApp confirmation
        String phone = formatPhone(order.getContact());
        if (phone != null) {
            String msg = "🎉 *Order Delivered!*... (Full message content here)";
            whatsappService.sendWhatsappMessage(phone, msg);
        }

        return true;
    }

    // ----------------------------------------------------
    // REPORTING LOGIC
    // ----------------------------------------------------

    public byte[] generateMonthlyReport(int year, int month, String format) {
        // Delegates the report generation (CSV/PDF file creation) to the ReportService
        return reportService.generateMonthlyDeliveredOrdersCsv(year, month);
    }

    // ----------------------------------------------------
    // UTILITY
    // ----------------------------------------------------

    /**
     * Formats the phone number to include country code +91 if necessary.
     */
    private String formatPhone(String phone) {
        if (phone == null || phone.isBlank()) return null;
        phone = phone.trim();
        if (!phone.startsWith("+")) {
            if (phone.length() == 10) phone = "+91" + phone;
            else phone = "+" + phone;
        }
        return phone;
    }
}