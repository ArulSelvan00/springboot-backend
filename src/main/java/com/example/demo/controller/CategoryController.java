package com.example.demo.controller;

import com.example.demo.model.Category; // Keep only necessary model imports
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "https://endearing-heliotrope-12d102.netlify.app")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // NOTE: The conflicting inner class and its dependencies (ShippingOrderRequest,
    // UserOrder, UserOrderRepository) have been removed from this file.

    @GetMapping
    public List<Category> getAllCategories() { return categoryRepository.findAll(); }

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(
            @RequestParam("name") String name, @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            if (image != null && !image.isEmpty()) { category.setImage(image.getBytes()); }
            return ResponseEntity.ok(categoryRepository.save(category));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id, @RequestParam("name") String name,
            @RequestParam("description") String description, @RequestParam(value = "image", required = false) MultipartFile image) {

        Optional<Category> existingCategoryOpt = categoryRepository.findById(id);
        if (existingCategoryOpt.isEmpty()) { return ResponseEntity.notFound().build(); }

        try {
            Category category = existingCategoryOpt.get();
            category.setName(name);
            category.setDescription(description);
            if (image != null && !image.isEmpty()) { category.setImage(image.getBytes()); }
            return ResponseEntity.ok(categoryRepository.save(category));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) { return ResponseEntity.notFound().build(); }
        try {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            // This is the constraint violation from Products/Subcategories
            return ResponseEntity.status(409).build();
        }
    }
}