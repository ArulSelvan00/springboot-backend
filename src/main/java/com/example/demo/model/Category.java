package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @Lob private byte[] image;

    // ⭐ CRITICAL FIX: @JsonIgnore to prevent infinite recursion loop
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SubCategory> subCategories;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    @JsonIgnore
    public List<SubCategory> getSubCategories() { return subCategories; }
    public void setSubCategories(List<SubCategory> subCategories) { this.subCategories = subCategories; }
}