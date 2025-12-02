package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.cache.annotation.Cacheable; // NEW
import org.springframework.cache.annotation.CacheEvict; // NEW
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(
        origins = "https://endearing-heliotrope-12d102.netlify.app",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // --- READ OPERATIONS ---

    // 🚀 CACHEABLE: Caches all categories. Key: categories::all
    @Cacheable(value = "categories", key = "'all'")
    @GetMapping
    public List<Category> getAllCategories() {
        System.out.println("--- DB CALL: Fetching all categories ---");
        return categoryRepository.findAll();
    }

    // 🚀 CACHEABLE: Caches category by ID. Key: categories::id
    @Cacheable(value = "categories", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        return categoryOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- CREATE OPERATION ---

    // 🧹 CACHE EVICT: Clears the entire 'categories' cache on creation.
    @CacheEvict(value = "categories", allEntries = true)
    @PostMapping
    public ResponseEntity<Category> addCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);

            if (image != null && !image.isEmpty()) {
                category.setImage(image.getBytes());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(category));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- UPDATE OPERATION ---

    // 🧹 CACHE EVICT: Clears the entire 'categories' cache on update.
    @CacheEvict(value = "categories", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Optional<Category> existingCategoryOpt = categoryRepository.findById(id);
        if (existingCategoryOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Category category = existingCategoryOpt.get();
            category.setName(name);
            category.setDescription(description);

            if (image != null && !image.isEmpty()) {
                category.setImage(image.getBytes());
            }

            return ResponseEntity.ok(categoryRepository.save(category));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- DELETE OPERATION ---

    // 🧹 CACHE EVICT: Clears the entire 'categories' cache on deletion.
    @CacheEvict(value = "categories", allEntries = true)
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}