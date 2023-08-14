package com.example.springboot0814training.service;

import com.example.springboot0814training.dto.ProductRequest;
import com.example.springboot0814training.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);
}
