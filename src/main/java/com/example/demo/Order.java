package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "orders") // optional, but avoids reserved keyword issues
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;
    private String orderDetails;

    // Constructors
    public Order() {}
    public Order(String phoneNumber, String orderDetails) {
        this.phoneNumber = phoneNumber;
        this.orderDetails = orderDetails;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getOrderDetails() { return orderDetails; }
    public void setOrderDetails(String orderDetails) { this.orderDetails = orderDetails; }
}
