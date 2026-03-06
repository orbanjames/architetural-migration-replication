package com.ecomapp.controller;

import com.ecomapp.models.Product;
import com.ecomapp.services.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/products/{id}")
    public Product getSingleProduct(@PathVariable int id) {
        return productService.getByID(id);
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAll();
    }
}