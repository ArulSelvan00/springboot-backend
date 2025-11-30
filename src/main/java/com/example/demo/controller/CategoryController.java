package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
// ✅ UPDATED CORS: Explicitly allow all methods, including OPTIONS (pre-flight check)
@CrossOrigin(
        origins = "https://endearing-heliotrope-12d102.netlify.app",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // --- READ OPERATIONS ---

    // GET: Fetch all categories (GET /api/categories)
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // GET: Fetch category by ID (GET /api/categories/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        return categoryOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- CREATE OPERATION ---

    // POST: Add a new category (POST /api/categories)
    // ✅ PATH CHANGE: Removed "/add" to follow REST convention
    @PostMapping
    public ResponseEntity<Category> addCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);

            // Check for image and set bytes
            if (image != null && !image.isEmpty()) {
                category.setImage(image.getBytes());
            }

            // Return 201 Created status for successful resource creation
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryRepository.save(category));

        } catch (IOException e) {
            // Handle file reading exception specifically
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- UPDATE OPERATION ---

    // PUT: Update an existing category (PUT /api/categories/{id})
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

            // Update image if a new one is provided
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

    // DELETE: Delete a category (DELETE /api/categories/{id})
    @DeleteMapping("/{id}")
    @Transactional // Ensures the deletion operation is atomic
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            // Status 409 Conflict: Typically used to indicate a resource
            // conflict, like a database constraint violation (e.g., this
            // category is still referenced by subcategories or products).
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}