package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_order")
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private Double totalPrice;
    private Integer quantity;
    private String paymentId;

    private String customerName;
    private String contact;
    private String pincode;
    private String village;
    private String address;
    private String orderDetails;
    private String category;

    // ✅ NEW FIELD: Store the unique Product ID for reliable stock lookup
    private Long productId;

    private String status = "pending";
    private LocalDateTime orderDate = LocalDateTime.now();

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getOrderDetails() { return orderDetails; }
    public void setOrderDetails(String orderDetails) { this.orderDetails = orderDetails; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public Long getProductId() { return productId; } // ✅ NEW GETTER
    public void setProductId(Long productId) { this.productId = productId; } // ✅ NEW SETTER
}