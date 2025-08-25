package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000") // Allow React frontend
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Create product with images
    @PostMapping(consumes = {"multipart/form-data"})
    public Product createProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam(required = false) MultipartFile coverImage,
            @RequestParam(required = false) MultipartFile image1,
            @RequestParam(required = false) MultipartFile image2,
            @RequestParam(required = false) MultipartFile image3
    ) throws IOException {

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);

        if (coverImage != null && !coverImage.isEmpty()) {
            product.setCoverImage(coverImage.getBytes());
        }
        if (image1 != null && !image1.isEmpty()) {
            product.setImage1(image1.getBytes());
        }
        if (image2 != null && !image2.isEmpty()) {
            product.setImage2(image2.getBytes());
        }
        if (image3 != null && !image3.isEmpty()) {
            product.setImage3(image3.getBytes());
        }

        return productRepository.save(product);
    }

    // Get all products (Base64 encoded images)
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get single product by ID (Base64 encoded images)
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }

    // Update product with images
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam int quantity,
            @RequestParam(required = false) MultipartFile coverImage,
            @RequestParam(required = false) MultipartFile image1,
            @RequestParam(required = false) MultipartFile image2,
            @RequestParam(required = false) MultipartFile image3
    ) throws IOException {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);

        if (coverImage != null && !coverImage.isEmpty()) {
            product.setCoverImage(coverImage.getBytes());
        }
        if (image1 != null && !image1.isEmpty()) {
            product.setImage1(image1.getBytes());
        }
        if (image2 != null && !image2.isEmpty()) {
            product.setImage2(image2.getBytes());
        }
        if (image3 != null && !image3.isEmpty()) {
            product.setImage3(image3.getBytes());
        }

        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().body("Product deleted successfully");
                }).orElse(ResponseEntity.notFound().build());
    }

    // Helper method to convert Product to ProductDTO with Base64 strings
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());

        dto.setCoverImage(product.getCoverImage() != null ? Base64.getEncoder().encodeToString(product.getCoverImage()) : null);
        dto.setImage1(product.getImage1() != null ? Base64.getEncoder().encodeToString(product.getImage1()) : null);
        dto.setImage2(product.getImage2() != null ? Base64.getEncoder().encodeToString(product.getImage2()) : null);
        dto.setImage3(product.getImage3() != null ? Base64.getEncoder().encodeToString(product.getImage3()) : null);

        return dto;
    }
}
