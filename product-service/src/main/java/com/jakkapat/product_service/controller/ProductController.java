package com.jakkapat.product_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jakkapat.product_service.dto.ProductRequest;
import com.jakkapat.product_service.dto.ProductResponse;
import com.jakkapat.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, String>> createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        // try {
        // Thread.sleep(5000);
        // } catch (InterruptedException e) {
        // throw new RuntimeException(e);
        // }
        return productService.getAllProducts();
    }

}
