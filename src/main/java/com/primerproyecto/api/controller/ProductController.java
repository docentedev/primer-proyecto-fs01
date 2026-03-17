package com.primerproyecto.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.primerproyecto.api.dto.ProductDTO;
import com.primerproyecto.api.dto.UserDTO;
import com.primerproyecto.api.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.list();
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO p) {
        return ResponseEntity.ok(productService.create(p));
    }
}
