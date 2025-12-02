package com.example.demo.controller;

import com.example.demo.model.SubCategory;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired private CategoryRepository categoryRepository;

    // --------------------------------------------------------------------------------
    // --- READ OPERATIONS (WITH CACHING) ---
    // --------------------------------------------------------------------------------

    // 🚀 CACHEABLE: Caches subcategories based on their parent category ID.
    @Cacheable(value = "subcategories", key = "#categoryId")
    @GetMapping("/by-category/{categoryId}")
    public List<SubCategory> getSubCategoriesByCategory(@PathVariable Long categoryId) {
        System.out.println("--- DB CALL: Fetching subcategories by category ID: " + categoryId + " ---");
        return subcategoryService.getSubcategoriesByCategoryId(categoryId);
    }

    // 🚀 CACHEABLE: Caches all subcategories.
    @Cacheable(value = "subcategories", key = "'all'")
    @GetMapping("/all")
    public List<SubCategory> getAllSubcategories() {
        System.out.println("--- DB CALL: Fetching all subcategories ---");
        return subcategoryService.findAll();
    }

    // 🚀 CACHEABLE: Caches a single subcategory by ID.
    @Cacheable(value = "subcategories", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable Long id) {
        Optional<SubCategory> subCategoryOpt = subcategoryService.findSubcategoryById(id);
        return subCategoryOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --------------------------------------------------------------------------------
    // --- CREATE OPERATION (FIXED: Uses @RequestPart for image) ---
    // --------------------------------------------------------------------------------

    // 🧹 CACHE EVICT: Clears the entire 'subcategories' cache on creation.
    // Maps to: POST /api/subcategories
    @CacheEvict(value = "subcategories", allEntries = true)
    @PostMapping
    public ResponseEntity<SubCategory> addSubcategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            // ✅ FIX: Use @RequestPart for optional MultipartFile handling in a multipart/form-data request
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            SubCategory subCategory = new SubCategory();
            subCategory.setName(name);
            subCategory.setDescription(description);
            subCategory.setCategory(categoryOpt.get());

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
    // --- UPDATE OPERATION (WITH CACHING) ---
    // --------------------------------------------------------------------------------

    // 🧹 CACHE EVICT: Clears the entire 'subcategories' cache on update.
    @CacheEvict(value = "subcategories", allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<SubCategory> updateSubcategory(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestPart(value = "image", required = false) MultipartFile image) {

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
    // --- DELETE OPERATION (WITH CACHING) ---
    // --------------------------------------------------------------------------------

    // 🧹 CACHE EVICT: Clears the entire 'subcategories' cache on deletion.
    @CacheEvict(value = "subcategories", allEntries = true)
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteSubcategory(@PathVariable Long id) {
        if (!subcategoryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            subcategoryService.deleteSubcategory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}