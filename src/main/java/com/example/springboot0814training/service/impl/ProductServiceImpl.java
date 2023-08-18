package com.example.springboot0814training.service.impl;

import com.example.springboot0814training.dao.ProductDAO;
import com.example.springboot0814training.dto.ProductRequest;
import com.example.springboot0814training.dto.ProductQueryParams;
import com.example.springboot0814training.model.Product;
import com.example.springboot0814training.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductDAO productDAO;

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    @Override
    public Product getProductById(Integer productId) {

        // 根據 productId 從資料庫中取得商品資料
        Product product = productDAO.getProductById(productId);

        // 如果商品資料為 null，則回傳 404 NOT_FOUND，並且在 console 中印出警告訊息
        if (product == null) {
            log.warn("Product with id {} not found", productId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            // 如果商品資料不為 null，則回傳該筆資料
            return product;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        // 根據 productRequest 建立商品資料
        return productDAO.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {

        // 根據 productId 從資料庫中取得商品資料
        Product product = productDAO.getProductById(productId);

        // 如果商品資料為 null，則回傳 404 NOT_FOUND，並且在 console 中印出警告訊息
        if (product == null) {
            log.warn("Product with id {} not found", productId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            // 如果商品資料不為 null，則更新該筆資料
            productDAO.updateProduct(productId, productRequest);
        }
    }

    @Override
    public void deleteProduct(Integer productId) {

        // 根據 productId 從資料庫中刪除該筆資料
        productDAO.deleteProduct(productId);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        // 根據 productQueryParams 從資料庫中篩選出符合條件的商品資料
        return productDAO.getProducts(productQueryParams);
    }

    @Override
    public Integer getTotalProducts(ProductQueryParams productQueryParams) {
        // 根據 productQueryParams 從資料庫中篩選出符合條件的商品總數
        return productDAO.getTotalProducts(productQueryParams);
    }
}
