package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.SubCategory;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private SubcategoryRepository subcategoryRepository;

    // ⭐ FIX: @Transactional(readOnly = true) ensures the session stays open for full serialization
    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product saveNewProduct(
            String name, String description, double price, int stock,
            Long subCategoryId, MultipartFile coverImageFile,
            MultipartFile image1File, MultipartFile image2File, MultipartFile image3File) throws IOException {

        Optional<SubCategory> subCategoryOpt = subcategoryRepository.findById(subCategoryId);
        if (subCategoryOpt.isEmpty()) { throw new RuntimeException("Subcategory not found."); }

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setSubCategory(subCategoryOpt.get());

        // Set Images
        if (coverImageFile == null || coverImageFile.isEmpty()) { throw new RuntimeException("Cover image is required."); }

        product.setCoverImage(coverImageFile.getBytes());
        if (image1File != null && !image1File.isEmpty()) product.setImage1(image1File.getBytes());
        if (image2File != null && !image2File.isEmpty()) product.setImage2(image2File.getBytes());
        if (image3File != null && !image3File.isEmpty()) product.setImage3(image3File.getBytes());

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(
            Long id, String name, String description, double price, int stock,
            Long subCategoryId, MultipartFile coverImageFile,
            MultipartFile image1File, MultipartFile image2File, MultipartFile image3File) throws IOException {

        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (existingProductOpt.isEmpty()) { throw new RuntimeException("Product not found with ID: " + id); }

        Optional<SubCategory> subCategoryOpt = subcategoryRepository.findById(subCategoryId);
        if (subCategoryOpt.isEmpty()) { throw new RuntimeException("Subcategory not found for update."); }

        Product product = existingProductOpt.get();

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setSubCategory(subCategoryOpt.get());

        if (coverImageFile != null && !coverImageFile.isEmpty()) product.setCoverImage(coverImageFile.getBytes());
        if (image1File != null && !image1File.isEmpty()) product.setImage1(image1File.getBytes());
        if (image2File != null && !image2File.isEmpty()) product.setImage2(image2File.getBytes());
        if (image3File != null && !image3File.isEmpty()) product.setImage3(image3File.getBytes());

        return productRepository.save(product);
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}