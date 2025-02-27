package com.castro.productsApi.controller;

import org.junit.jupiter.api.Test;

import com.castro.productsApi.model.Product;
import com.castro.productsApi.repository.ProductRepository;
import com.castro.productsApi.controller.ProductController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    private final Product product = Product.builder()
            .id("123")
            .name("Product A")
            .description("Description A")
            .price(100.0)
            .build();

    @Test
    void testSaveProductSuccessfully() {
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<Product> response = productController.saveProduct(product);

        assertEquals(201, HttpStatus.CREATED.value());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testSaveProductExceptionThrown() {
        doThrow(new IllegalArgumentException("Invalid product data"))
                .when(productRepository).save(any(Product.class));

        Product invalidProduct = Product.builder().build();

        ResponseEntity<Product> response = productController.saveProduct(invalidProduct);

        assertEquals(422, HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertNull(response.getBody());
    }

    @Test
    void testGetProductSuccessfully() {
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));

        ResponseEntity<Product> response = productController.getProduct("123");

        assertEquals(200, HttpStatus.OK.value());
        assertNotNull(response.getBody());
        assertEquals("123", response.getBody().getId());
        verify(productRepository, times(1)).findById(anyString());
    }

    @Test
    void testGetProductNotFound() {
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProduct("123");

        assertEquals(404, HttpStatus.NOT_FOUND.value());
        assertNull(response.getBody());
        verify(productRepository, times(1)).findById(anyString());
    }
}