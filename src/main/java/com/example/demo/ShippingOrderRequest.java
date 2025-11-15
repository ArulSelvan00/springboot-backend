package com.example.demo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class ShippingOrderRequest {
    private String productName;
    private double productPrice;
    private int quantity;
    private String customerName;
    private String contactNumber;   // optional: same as phoneNumber
    private String phoneNumber;     // used for Twilio
    private String pincode;
    private String villageAddress;
    private String address;
    private String village;
    private String orderDetails;

    // Getters and setters
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getProductPrice() { return productPrice; }
    public void setProductPrice(double productPrice) { this.productPrice = productPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getVillageAddress() { return villageAddress; }
    public void setVillageAddress(String villageAddress) { this.villageAddress = villageAddress; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }

    public String getOrderDetails() { return orderDetails; }
    public void setOrderDetails(String orderDetails) { this.orderDetails = orderDetails; }

    @Entity
    @Table(name = "user_order")
    public static class UserOrder {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String productName;
        private Double productPrice;
        private Integer quantity;
        private String customerName;
        private String contactNumber;
        private String phoneNumber;
        private String pincode;
        private String village;
        private String address;
        private String orderDetails;
        private String category;
        private String status = "pending";
        private LocalDateTime orderDate = LocalDateTime.now();

        // Getters & Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Double getProductPrice() { return productPrice; }
        public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getContactNumber() { return contactNumber; }
        public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public String getPincode() { return pincode; }
        public void setPincode(String pincode) { this.pincode = pincode; }

        public String getVillage() { return village; }
        public void setVillage(String village) { this.village = village; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public String getOrderDetails() { return orderDetails; }
        public void setOrderDetails(String orderDetails) { this.orderDetails = orderDetails; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public LocalDateTime getOrderDate() { return orderDate; }
        public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    }

    public static class UserOrderRepository {
    }
}
