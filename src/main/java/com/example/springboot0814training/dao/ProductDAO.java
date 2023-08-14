package com.example.springboot0814training.dao;

import com.example.springboot0814training.model.Product;

public interface ProductDAO {

    Product getProductById(Integer productId);
}
