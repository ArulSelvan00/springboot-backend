package com.example.demo.controller;

import com.example.demo.model.SubCategory;
import com.example.demo.model.Category; // Needed to link to Category
import com.example.demo.repository.CategoryRepository; // Needed to fetch parent Category
import com.example.demo.service.SubcategoryService; // Assume you have this service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import jakarta.transaction.Transactional; // Use jakarta.transaction.Transactional consistent with your Category code

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subcategories")
@CrossOrigin(
        origins = "https://endearing-heliotrope-12d102.netlify.app",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class SubcategoryController {

    @Autowired private SubcategoryService subcategoryService;
    @Autowired private CategoryRepository categoryRepository; // To find the parent category

    // --------------------------------------------------------------------------------
    // --- READ OPERATIONS (WITH CACHING) ---
    // --------------------------------------------------------------------------------

    // 🚀 CACHEABLE: Caches all subcategories. Key: subcategories::all
    @Cacheable(value = "subcategories", key = "'all'")
    @GetMapping("/all")
    public List<SubCategory> getAllSubcategories() {
        System.out.println("--- DB CALL: Fetching all subcategories ---");
        return subcategoryService.findAll();
    }

    // 🚀 CACHEABLE: Caches subcategories based on their parent category ID. Key: subcategories::categoryId
    @Cacheable(value = "subcategories", key = "#categoryId")
    @GetMapping("/by-category/{categoryId}")
    public List<SubCategory> getSubCategoriesByCategory(@PathVariable Long categoryId) {
        System.out.println("--- DB CALL: Fetching subcategories by category ID: " + categoryId + " ---");
        return subcategoryService.getSubcategoriesByCategoryId(categoryId);
    }

    // 🚀 CACHEABLE: Caches a single subcategory by ID. Key: subcategories::id
    @Cacheable(value = "subcategories", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable Long id) {
        Optional<SubCategory> subCategoryOpt = subcategoryService.findSubcategoryById(id);
        return subCategoryOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --------------------------------------------------------------------------------
    // --- CREATE OPERATION (POST /api/subcategories) ---
    // --------------------------------------------------------------------------------

    // 🧹 CACHE EVICT: Clears the entire 'subcategories' cache on creation.
    @CacheEvict(value = "subcategories", allEntries = true)
    @PostMapping
    public ResponseEntity<SubCategory> addSubcategory(
            // Handles text fields from FormData
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId, // New field to link the parent category
            // Handles the file part from FormData (using @RequestParam like your Category code)
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                // If the Category ID doesn't exist, return a clear error
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            SubCategory subCategory = new SubCategory();
            subCategory.setName(name);
            subCategory.setDescription(description);
            subCategory.setCategory(categoryOpt.get()); // Set the parent category object

            if (image != null && !image.isEmpty()) {
                subCategory.setImage(image.getBytes());
            }

            SubCategory savedSubCategory = subcategoryService.saveSubcategory(subCategory);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedSubCategory);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --------------------------------------------------------------------------------
    // --- UPDATE OPERATION (PUT /api/subcategories/{id}) ---
    // --------------------------------------------------------------------------------

    // 🧹 CACHE EVICT: Clears the entire 'subcategories' cache on update.
    @CacheEvict(value = "subcategories", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<SubCategory> updateSubcategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId, // Allows changing the parent category
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Optional<SubCategory> existingSubcategoryOpt = subcategoryService.findSubcategoryById(id);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (existingSubcategoryOpt.isEmpty() || categoryOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            SubCategory subCategory = existingSubcategoryOpt.get();
            subCategory.setName(name);
            subCategory.setDescription(description);
            subCategory.setCategory(categoryOpt.get());

            if (image != null && !image.isEmpty()) {
                subCategory.setImage(image.getBytes());
            }

            SubCategory updatedSubCategory = subcategoryService.saveSubcategory(subCategory);

            return ResponseEntity.ok(updatedSubCategory);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --------------------------------------------------------------------------------
    // --- DELETE OPERATION (DELETE /api/subcategories/{id}) ---
    // --------------------------------------------------------------------------------

    // 🧹 CACHE EVICT: Clears the entire 'subcategories' cache on deletion.
    @CacheEvict(value = "subcategories", allEntries = true)
    @DeleteMapping("/{id}")
    @Transactional // Ensures the deletion is atomic
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        if (!subcategoryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            subcategoryService.deleteSubcategory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            // CONFLICT status suggests a foreign key constraint (e.g., linked products) is blocking the delete
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}