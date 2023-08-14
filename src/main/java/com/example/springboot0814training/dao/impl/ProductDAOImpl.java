package com.example.springboot0814training.dao.impl;

import com.example.springboot0814training.dao.ProductDAO;
import com.example.springboot0814training.model.Product;
import com.example.springboot0814training.rowmapper.ProductRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDAOImpl implements ProductDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ProductDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> products = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if (products.size() > 0) {
            return products.get(0);
        } else {
            return null;
        }
    }
}
