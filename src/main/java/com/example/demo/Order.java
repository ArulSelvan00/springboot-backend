package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private int quantity;
    private double totalPrice;
    private String paymentId;
    private String village;
    private String pincode;
    private String address;
    private String contact;

    public Order() {}

    public Order(String productName, int quantity, double totalPrice, String paymentId,
                 String village, String pincode, String address, String contact) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.paymentId = paymentId;
        this.village = village;
        this.pincode = pincode;
        this.address = address;
        this.contact = contact;
    }

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getVillage() { return village; }
    public void setVillage(String village) { this.village = village; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
}
