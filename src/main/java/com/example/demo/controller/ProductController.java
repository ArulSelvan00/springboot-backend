package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException; // Import for explicit file handling exceptions
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
// ✅ UPDATED CORS: Explicitly allow all required HTTP methods for robust cross-origin stability.
@CrossOrigin(
        origins = "https://endearing-heliotrope-12d102.netlify.app",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class ProductController {

    @Autowired
    private ProductService productService;

    // --------------------------------------------------------------------------------
    // --- READ OPERATIONS ---
    // --------------------------------------------------------------------------------

    // GET: Fetch all products (GET /api/products)
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    // GET: Fetch product by ID (GET /api/products/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = productService.findProductById(id);
        return productOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --------------------------------------------------------------------------------
    // --- CREATE OPERATION ---
    // --------------------------------------------------------------------------------

    // POST: Create a new product (POST /api/products)
    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestParam("name") String name, @RequestParam("description") String description,
            @RequestParam("price") double price, @RequestParam("stock") int stock,
            @RequestParam("subCategoryId") Long subCategoryId,
            // Uses @RequestPart for file handling in multipart/form-data
            @RequestPart("coverImage") MultipartFile coverImageFile, // REQUIRED FILE
            @RequestPart(value = "image1", required = false) MultipartFile image1File,
            @RequestPart(value = "image2", required = false) MultipartFile image2File,
            @RequestPart(value = "image3", required = false) MultipartFile image3File) {

        try {
            Product newProduct = productService.saveNewProduct(
                    name, description, price, stock, subCategoryId,
                    coverImageFile, image1File, image2File, image3File
            );
            // Return 201 Created status
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (RuntimeException e) {
            // Catches custom business logic exceptions (e.g., SubCategory not found)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            // ⚠️ IMPROVED: Explicitly catch file reading/IO errors
            return new ResponseEntity<>("File processing failed.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An unexpected error occurred during creation.");
        }
    }

    // --------------------------------------------------------------------------------
    // --- UPDATE OPERATION ---
    // --------------------------------------------------------------------------------

    // PUT: Update an existing product (PUT /api/products/{id})
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
            // Catches Product not found or other business logic errors
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            // ⚠️ IMPROVED: Explicitly catch file reading/IO errors
            return new ResponseEntity<>("File processing failed.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --------------------------------------------------------------------------------
    // --- DELETE OPERATION ---
    // --------------------------------------------------------------------------------

    // DELETE: Delete a product (DELETE /api/products/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            boolean deleted = productService.deleteProduct(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Status 409 Conflict: Typically if the product is linked to an order/cart item
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}