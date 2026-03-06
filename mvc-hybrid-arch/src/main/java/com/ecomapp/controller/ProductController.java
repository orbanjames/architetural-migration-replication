package com.ecomapp.controller;

import com.ecomapp.models.Product;
import com.ecomapp.services.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/products/{id}")
    public CompletableFuture<Product> getSingleProduct(@PathVariable int id) {
        return productService.getByID(id);
    }

    @GetMapping("/products")
    public CompletableFuture<List<Product>> getAllProducts() {
        return productService.getAll();
    }
}