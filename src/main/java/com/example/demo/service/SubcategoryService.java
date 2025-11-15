package com.example.demo.service;

import com.example.demo.model.SubCategory;
import com.example.demo.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    // --- READ OPERATIONS ---

    // Fetches ALL subcategories (needed for the CategoryForm component rendering the list)
    public List<SubCategory> findAll() {
        return subcategoryRepository.findAll();
    }

    // Finds a single subcategory (needed for Update logic)
    public Optional<SubCategory> findSubcategoryById(Long id) {
        return subcategoryRepository.findById(id);
    }

    // Finds all subcategories by the parent Category ID (CRITICAL for Product Manager dropdown)
    public List<SubCategory> getSubcategoriesByCategoryId(Long categoryId) {
        return subcategoryRepository.findByCategory_Id(categoryId);
    }

    public boolean existsById(Long id) {
        return subcategoryRepository.existsById(id);
    }

    // --- WRITE OPERATIONS ---

    @Transactional
    public SubCategory saveSubcategory(SubCategory subCategory) {
        // Used for both Add and Update operations
        return subcategoryRepository.save(subCategory);
    }

    @Transactional
    public void deleteSubcategory(Long id) {
        subcategoryRepository.deleteById(id);
    }
}