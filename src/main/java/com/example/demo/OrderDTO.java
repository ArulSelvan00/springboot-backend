package com.example.demo;

public class OrderDTO {
    private String productName;
    private int quantity;
    private double totalPrice;
    private String paymentId;
    private String village;
    private String pincode;
    private String address;
    private String contact;

    public OrderDTO() {}

    // getters & setters
    public String getProductName() {return productName;}
    public void setProductName(String productName) {this.productName = productName;}
    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}
    public double getTotalPrice() {return totalPrice;}
    public void setTotalPrice(double totalPrice) {this.totalPrice = totalPrice;}
    public String getPaymentId() {return paymentId;}
    public void setPaymentId(String paymentId) {this.paymentId = paymentId;}
    public String getVillage() {return village;}
    public void setVillage(String village) {this.village = village;}
    public String getPincode() {return pincode;}
    public void setPincode(String pincode) {this.pincode = pincode;}
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    public String getContact() {return contact;}
    public void setContact(String contact) {this.contact = contact;}
}
