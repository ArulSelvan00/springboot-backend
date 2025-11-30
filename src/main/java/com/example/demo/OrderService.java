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
    // @Autowired private ReportService reportService; // Commented out if not fully defined
    @Autowired private TwilioWhatsappService whatsappService;

    // ----------------------------------------------------
    // CUSTOMER FACING LOGIC (Place Order & Stock Update)
    // ----------------------------------------------------

    /**
     * Handles order placement: checks stock, updates stock, saves order, and sends confirmation.
     * @throws RuntimeException if stock is insufficient or product not found.
     */
    @Transactional
    public UserOrder placeShippingOrder(UserOrder order) {
        Long productId = order.getProductId();
        Integer orderedQuantity = order.getQuantity();

        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            int currentStock = product.getStock();

            if (currentStock >= orderedQuantity) {
                // Update Stock
                product.setStock(currentStock - orderedQuantity);
                productRepository.save(product);
            } else {
                throw new RuntimeException("Stock update failed: Insufficient stock for " + product.getName() + ". Available: " + currentStock);
            }
        } else {
            throw new RuntimeException("Product not found for stock update.");
        }

        // Set initial status and save order
        order.setStatus("pending");
        UserOrder savedOrder = userOrderRepository.save(order);

        // Send initial confirmation
        String phone = formatPhone(order.getContact());
        if (phone != null) {
            String msg = String.format("🎉 *Order Confirmed!* \n\nProduct: %s (x%d), Total: ₹%.2f. We will notify you when it's out for delivery.",
                    order.getProductName(), order.getQuantity(), order.getTotalPrice());
            whatsappService.sendWhatsappMessage(phone, msg);
        }

        return savedOrder;
    }

    // ----------------------------------------------------
    // ADMIN STATUS & HISTORY LOGIC
    // ----------------------------------------------------

    /**
     * Retrieves all active/pending orders.
     */
    public List<UserOrder> getAllOrders() {
        // In a real app, this should filter out 'delivered' orders.
        // Assuming the repository/DB query handles filtering by status != 'delivered'.
        return userOrderRepository.findAll();
    }

    /**
     * Marks an order as 'out_for_delivery' and sends notification.
     */
    @Transactional
    public boolean markOutForDelivery(Long id) {
        Optional<UserOrder> orderOpt = userOrderRepository.findById(id);
        if (orderOpt.isEmpty()) return false;

        UserOrder order = orderOpt.get();
        order.setStatus("out_for_delivery");
        userOrderRepository.save(order);

        String phone = formatPhone(order.getContact());
        if (phone != null) {
            String msg = String.format("🚚 *Order Out for Delivery!* \n\nYour order #%d is on its way to %s.",
                    order.getId(), order.getVillage());
            whatsappService.sendWhatsappMessage(phone, msg);
        }

        return true;
    }

    /**
     * Retrieves all delivered orders history.
     */
    public List<DeliveredOrder> getDeliveredOrders() {
        return deliveredOrderRepository.findAll();
    }

    /**
     * Moves an order from UserOrder to DeliveredOrder and deletes the UserOrder.
     */
    @Transactional
    public boolean moveToDelivered(Long id) {
        Optional<UserOrder> orderOpt = userOrderRepository.findById(id);
        if (orderOpt.isEmpty()) return false;

        UserOrder order = orderOpt.get();

        // 1. Create DeliveredOrder and copy all necessary data
        DeliveredOrder delivered = new DeliveredOrder();
        delivered.setOrderId(order.getId());
        delivered.setProductName(order.getProductName());
        delivered.setCustomerName(order.getCustomerName());
        delivered.setQuantity(order.getQuantity()); // Added quantity
        delivered.setStatus("delivered");
        delivered.setTotalPrice(order.getTotalPrice());
        delivered.setContact(order.getContact());
        delivered.setAddress(order.getAddress());
        delivered.setVillage(order.getVillage());
        delivered.setPincode(order.getPincode());
        delivered.setDeliveredDate(new Date());

        deliveredOrderRepository.save(delivered);

        // 2. Remove from pending list
        userOrderRepository.deleteById(id);

        // 3. Send WhatsApp confirmation
        String phone = formatPhone(order.getContact());
        if (phone != null) {
            String msg = String.format("✅ *Order Delivered!* \n\nYour order #%d has been successfully delivered. Thank you!",
                    order.getId());
            whatsappService.sendWhatsappMessage(phone, msg);
        }

        return true;
    }

    // ----------------------------------------------------
    // REPORTING LOGIC
    // ----------------------------------------------------

    public byte[] generateMonthlyReport(int year, int month, String format) {
        // Placeholder: Needs a concrete implementation in ReportService
        return new byte[0]; // reportService.generateMonthlyDeliveredOrdersCsv(year, month);
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
        // Assuming India code +91 for 10-digit numbers
        if (!phone.startsWith("+")) {
            if (phone.length() == 10) phone = "+91" + phone;
            else phone = "+" + phone;
        }
        return phone;
    }
}