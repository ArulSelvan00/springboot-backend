package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "product") // ✅ Explicitly set table name
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private int quantity;

    // Store images as LONGBLOB
    @Lob
    @Column(name = "cover_image", columnDefinition = "LONGBLOB")
    private byte[] coverImage;

    @Lob
    @Column(name = "image1", columnDefinition = "LONGBLOB")
    private byte[] image1;

    @Lob
    @Column(name = "image2", columnDefinition = "LONGBLOB")
    private byte[] image2;

    @Lob
    @Column(name = "image3", columnDefinition = "LONGBLOB")
    private byte[] image3;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public byte[] getCoverImage() { return coverImage; }
    public void setCoverImage(byte[] coverImage) { this.coverImage = coverImage; }

    public byte[] getImage1() { return image1; }
    public void setImage1(byte[] image1) { this.image1 = image1; }

    public byte[] getImage2() { return image2; }
    public void setImage2(byte[] image2) { this.image2 = image2; }

    public byte[] getImage3() { return image3; }
    public void setImage3(byte[] image3) { this.image3 = image3; }
}
