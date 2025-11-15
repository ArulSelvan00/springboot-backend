package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private int stock; // Used for stock management

    @Lob private byte[] coverImage;
    @Lob private byte[] image1;
    @Lob private byte[] image2;
    @Lob private byte[] image3;

    // ⭐ FIX: EAGER Fetching to ensure SubCategory data loads with Product
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public byte[] getCoverImage() { return coverImage; }
    public void setCoverImage(byte[] coverImage) { this.coverImage = coverImage; }
    public byte[] getImage1() { return image1; }
    public void setImage1(byte[] image1) { this.image1 = image1; }
    public byte[] getImage2() { return image2; }
    public void setImage2(byte[] image2) { this.image2 = image2; }
    public byte[] getImage3() { return image3; }
    public void setImage3(byte[] image3) { this.image3 = image3; }
    public SubCategory getSubCategory() { return subCategory; }
    public void setSubCategory(SubCategory subCategory) { this.subCategory = subCategory; }
}