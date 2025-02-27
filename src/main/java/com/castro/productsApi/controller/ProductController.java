package com.castro.productsApi.controller;

import com.castro.productsApi.model.Product;
import com.castro.productsApi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    @PostMapping(value = "/save")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        product.setId(UUID.randomUUID().toString());
        log.info("Saving product: {}", product);

        try {
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @GetMapping(value = "/get")
    public ResponseEntity<Product> getProduct(@RequestParam String id) {
        log.info("Getting product with id: {}", id);
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
