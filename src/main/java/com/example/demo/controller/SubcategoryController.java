package com.example.demo.controller;

import com.example.demo.model.SubCategory;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository; // Required to find parent category
import com.example.demo.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subcategories")
@CrossOrigin(origins = "https://endearing-heliotrope-12d102.netlify.app")
public class SubcategoryController {

    @Autowired private SubcategoryService subcategoryService;
    @Autowired private CategoryRepository categoryRepository;

    // --- READ OPERATIONS ---

    // GET /api/subcategories/by-category/{categoryId} (CRITICAL for dropdown loading)
    @GetMapping("/by-category/{categoryId}")
    public List<SubCategory> getSubCategoriesByCategory(@PathVariable Long categoryId) {
        return subcategoryService.getSubcategoriesByCategoryId(categoryId);
    }

    // GET /api/subcategories/all (Used by CategoryForm to display the existing list)
    @GetMapping("/all")
    public List<SubCategory> getAllSubcategories() {
        return subcategoryService.findAll();
    }

    // --- CREATE OPERATION ---
    // POST /api/subcategories/add
    @PostMapping("/add")
    public ResponseEntity<SubCategory> addSubcategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId, // Parent category ID
            @RequestParam(value = "image", required = false) MultipartFile image) {

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
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- UPDATE OPERATION ---
    // PUT /api/subcategories/{id}
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
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- DELETE OPERATION ---
    // DELETE /api/subcategories/{id}
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