package com.example.springboot0814training.controller;

import com.example.springboot0814training.constant.ProductCategory;
import com.example.springboot0814training.dto.ProductRequest;
import com.example.springboot0814training.dto.ProductQueryParams;
import com.example.springboot0814training.model.Product;
import com.example.springboot0814training.service.ProductService;
import com.example.springboot0814training.util.Page;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(@RequestParam(required = false) String search,
                                                     @RequestParam(required = false) ProductCategory category,
                                                     @RequestParam(defaultValue = "created_date") String orderBy,
                                                     @RequestParam(defaultValue = "desc") String sort,
                                                     @RequestParam(defaultValue = "1") Integer page) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setSearch(search);
        productQueryParams.setCategory(category);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setPage(page);

        Integer total = productService.getTotalProducts(productQueryParams);

        List<Product> products = productService.getProducts(productQueryParams);

        Page<Product> productPage = new Page<>();
        productPage.setPage(page);
        productPage.setTotal(total);
        productPage.setResults(products);

        return ResponseEntity.status(200).body(productPage);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.status(200).body(product);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {

        Integer productId = productService.createProduct(productRequest);

        Product product = productService.getProductById(productId);

        return ResponseEntity.status(201).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId, @RequestBody @Valid ProductRequest productRequest) {
        productService.updateProduct(productId, productRequest);

        Product updatedProduct = productService.getProductById(productId);

        return ResponseEntity.status(200).body(updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);

        return ResponseEntity.status(204).build();
    }
}
