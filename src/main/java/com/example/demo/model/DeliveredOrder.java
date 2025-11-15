package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "delivered_orders")
public class DeliveredOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId; // Original ID from UserOrder
    private String productName;
    private String customerName;
    private String status;
    private Double totalPrice;
    private String contact;
    private String address;
    private String village;
    private String pincode;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveredDate;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public Date getDeliveredDate() { return deliveredDate; }
    public void setDeliveredDate(Date deliveredDate) { this.deliveredDate = deliveredDate; }
}