package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "https://endearing-heliotrope-12d102.netlify.app/")
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET: Fetch all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    // GET: Fetch product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = productService.findProductById(id);
        return productOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: Create a new product (Requires 'form-data' request body)
    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name, @RequestParam("description") String description,
            @RequestParam("price") double price, @RequestParam("stock") int stock,
            @RequestParam("subCategoryId") Long subCategoryId,
            @RequestPart("coverImage") MultipartFile coverImageFile, // REQUIRED FILE
            @RequestPart(value = "image1", required = false) MultipartFile image1File,
            @RequestPart(value = "image2", required = false) MultipartFile image2File,
            @RequestPart(value = "image3", required = false) MultipartFile image3File) {

        try {
            Product newProduct = productService.saveNewProduct(
                    name, description, price, stock, subCategoryId,
                    coverImageFile, image1File, image2File, image3File
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An unexpected error occurred during creation.");
        }
    }

    // PUT: Update an existing product
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name, @RequestParam("description") String description,
            @RequestParam("price") double price, @RequestParam("stock") int stock,
            @RequestParam("subCategoryId") Long subCategoryId,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImageFile,
            @RequestPart(value = "image1", required = false) MultipartFile image1File,
            @RequestPart(value = "image2", required = false) MultipartFile image2File,
            @RequestPart(value = "image3", required = false) MultipartFile image3File) {
        try {
            Product updatedProduct = productService.updateProduct(
                    id, name, description, price, stock, subCategoryId,
                    coverImageFile, image1File, image2File, image3File
            );
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE: Delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            boolean deleted = productService.deleteProduct(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}