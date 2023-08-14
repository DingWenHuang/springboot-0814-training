package com.example.springboot0814training.service.impl;

import com.example.springboot0814training.dao.ProductDAO;
import com.example.springboot0814training.model.Product;
import com.example.springboot0814training.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    @Override
    public Product getProductById(Integer productId) {
        Product product = productDAO.getProductById(productId);

        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return product;
        }
    }
}
