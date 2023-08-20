package com.example.springboot0814training.dao.impl;

import com.example.springboot0814training.dao.ProductDAO;
import com.example.springboot0814training.dto.ProductRequest;
import com.example.springboot0814training.dto.ProductQueryParams;
import com.example.springboot0814training.model.Product;
import com.example.springboot0814training.rowmapper.ProductRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

        // 創建SQL語句
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE product_id = :productId";

        // 創建Map對象用來設置SQL語句中的參數，這裡是根據productId查詢商品，所以需要設置productId這個參數
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        // 查詢商品
        List<Product> products = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        // 如果查詢到商品，則返回第一個商品，否則返回null
        if (products.size() > 0) {
            return products.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {

        // 創建SQL語句
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date) VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";

        // 創建Map對象用來設置SQL語句中的參數
        // 設置的參數包括商品名稱、類別、圖片URL、價格、庫存、描述、創建時間、最後修改時間
        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().name());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        // 取得當前時間用來設置SQL語句中的參數
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        // 創建KeyHolder對象用來獲取創建的商品的ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        // 獲取創建的商品的ID
        Integer productId = keyHolder.getKey().intValue();

        return productId ;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {

        // 創建SQL語句
        String sql = "UPDATE product SET product_name = :productName, category = :category, image_url = :imageUrl, price = :price, stock = :stock, description = :description, last_modified_date = :lastModifiedDate WHERE product_id = :productId";

        // 創建Map對象用來設置SQL語句中的參數，設置的參數包括要更新的目標商品ID、商品名稱、類別、圖片URL、價格、庫存、描述、最後修改時間
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().name());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        // 取得當前時間用來設置SQL語句中的參數，這裡要更新最後修改時間
        Date now = new Date();
        map.put("lastModifiedDate", now);

        // 執行更新
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteProductById(Integer productId) {

        // 創建SQL語句
        String sql = "DELETE FROM product WHERE product_id = :productId";

        // 創建Map對象用來設置SQL語句中的參數，這裡是根據productId刪除商品，所以需要設置productId這個參數
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        // 執行刪除
        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {

        // 創建SQL語句
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE 1 = 1";

        // 創建Map對象用來設置SQL語句中的參數
        Map<String, Object> map = new HashMap<>();

        // 添加過濾條件
        sql = addFilteringSQL(sql, map, productQueryParams);

        // 根據排序條件設置SQL語句中的參數
        sql += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        // 取得單個分頁要顯示的數據量
        Integer limit = productQueryParams.getLimit();

        // 計算分頁的偏移與要取得的數據量
        // 例如LIMIT 5,5 ，表示從第6條數據開始，總共取5條數據
        sql += " LIMIT " + (productQueryParams.getPage() - 1) * limit + ", " + limit;

        // 執行查詢，並返回查詢結果
        return namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
    }

    @Override
    public Integer countProducts(ProductQueryParams productQueryParams) {

        // 創建SQL語句
        String sql = "SELECT COUNT(*) FROM product WHERE 1 = 1";

        // 創建Map對象用來設置SQL語句中的參數
        Map<String, Object> map = new HashMap<>();

        // 添加過濾條件
        sql = addFilteringSQL(sql, map, productQueryParams);

        // 執行查詢，並返回查詢結果
        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    @Override
    public void updateStock(Integer productId, Integer newStock) {

        // 創建SQL語句
        String sql = "UPDATE product SET stock = :newStock, last_modified_date = :lastModifiedDate WHERE product_id = :productId";

        // 創建Map對象用來設置SQL語句中的參數，設置的參數包括要更新的目標商品ID、新的庫存、最後修改時間
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("newStock", newStock);
        map.put("lastModifiedDate", new Date());

        // 執行更新
        namedParameterJdbcTemplate.update(sql, map);
    }

    private String addFilteringSQL(String sql, Map<String, Object> map, ProductQueryParams productQueryParams) {

        // 如果有傳入商品名稱，則添加過濾條件，進行模糊查詢
        if (productQueryParams.getSearch() != null) {
            sql += " AND product_name LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() + "%");
        }

        // 如果有傳入商品類別，則添加過濾條件
        if (productQueryParams.getCategory() != null) {
            sql += " AND category = :category";
            map.put("category", productQueryParams.getCategory().name());
        }

        return sql;
    }

}
