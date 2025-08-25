package com.example.demo;

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
}
