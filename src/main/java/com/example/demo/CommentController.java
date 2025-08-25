package com.example.demo;

import com.example.demo.Comment;
import com.example.demo.Product;
import com.example.demo.CommentRepository;
import com.example.demo.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;

    public CommentController(CommentRepository commentRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/{productId}")
    public Comment addComment(@PathVariable Long productId, @RequestBody Comment comment) {
        Product product = productRepository.findById(productId).orElseThrow();
        comment.setProduct(product);
        return commentRepository.save(comment);
    }

    @GetMapping("/{productId}")
    public List<Comment> getComments(@PathVariable Long productId) {
        return commentRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }
}
