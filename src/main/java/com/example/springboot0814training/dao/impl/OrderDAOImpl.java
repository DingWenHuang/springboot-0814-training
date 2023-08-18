package com.example.springboot0814training.dao.impl;

import com.example.springboot0814training.dao.OrderDAO;
import com.example.springboot0814training.dto.OrderQueryParams;
import com.example.springboot0814training.model.Order;
import com.example.springboot0814training.model.OrderItem;
import com.example.springboot0814training.rowmapper.OrderItemRowMapper;
import com.example.springboot0814training.rowmapper.OrderRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrderDAOImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Integer createOrder(Integer userId, BigDecimal totalAmount) {

        // 創建SQL語句
        String sql = "INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date) VALUES (:userId, :totalAmount,:createdDate, :lastModifiedDate)";

        // 創建Map對象用來設置SQL語句中的參數，這裡是根據userId和totalAmount創建訂單，所以需要設置userId和totalAmount這兩個參數
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);

        // 取得當前時間用來設置SQL語句中的參數
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        // 創建KeyHolder對象用來獲取創建的訂單的ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        // 獲取創建的訂單的ID
        Integer orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItem(Integer orderId, List<OrderItem> orderItemList) {

        // 創建SQL語句
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount) VALUES (:orderId, :productId, :quantity, :amount)";

        // 創建SqlParameterSource對象用來設置SQL語句中的參數
        MapSqlParameterSource[] mapSqlParameterSources = new MapSqlParameterSource[orderItemList.size()];

        // 遍歷orderItemList，為每個OrderItem創建一個MapSqlParameterSource對象
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);

            // 設置MapSqlParameterSource對象中的參數
            mapSqlParameterSources[i] = new MapSqlParameterSource();
            mapSqlParameterSources[i].addValue("orderId", orderId);
            mapSqlParameterSources[i].addValue("productId", orderItem.getProductId());
            mapSqlParameterSources[i].addValue("quantity", orderItem.getQuantity());
            mapSqlParameterSources[i].addValue("amount", orderItem.getAmount());
        }

        // 執行批量更新
        namedParameterJdbcTemplate.batchUpdate(sql, mapSqlParameterSources);
    }

    @Override
    public Order getOrderById(Integer orderId) {

        // 創建SQL語句
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date FROM `order` WHERE order_id = :orderId";

        // 創建Map對象用來設置SQL語句中的參數，這裡是根據orderId查詢，所以只需要設置orderId這個參數
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        // 執行查詢，並返回查詢結果
        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        // 如果查詢結果不為空，則返回查詢結果中的第一個元素，否則返回null
        if (orderList.size() > 0) {
            return orderList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {

        // 創建SQL語句
        // 這裡使用LEFT JOIN來查詢訂單項目對應的商品，取得商品的名稱和圖片URL
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url FROM order_item oi LEFT JOIN product p ON oi.product_id = p.product_id WHERE oi.order_id = :orderId";

        // 創建Map對象用來設置SQL語句中的參數，這裡是根據orderId查詢，所以只需要設置orderId這個參數
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        // 執行查詢，並返回查詢結果
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemList;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {

        // 創建SQL語句
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date FROM `order` WHERE 1 = 1";

        // 創建Map對象用來設置SQL語句中的參數
        Map<String, Object> map = new HashMap<>();

        // 添加過濾條件
        sql = addFilteringSql(sql, map, orderQueryParams);

        // 根據創建時間降序排序
        sql += " ORDER BY created_date DESC";

        // 取得單個分頁要顯示的數據量
        Integer limit = orderQueryParams.getLimit();

        // 計算分頁的偏移與要取得的數據量
        // 例如LIMIT 5,5 ，表示從第6條數據開始，總共取5條數據
        sql += " LIMIT " + (orderQueryParams.getPage() - 1) * limit + ", " + limit;

        // 執行查詢，並返回查詢結果
        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        return orderList;
    }

    @Override
    public Integer countOrders(OrderQueryParams orderQueryParams) {

        // 創建SQL語句
        String sql = "SELECT COUNT(*) FROM `order` WHERE 1 = 1";

        // 創建Map對象用來設置SQL語句中的參數
        Map<String, Object> map = new HashMap<>();

        // 添加過濾條件
        sql = addFilteringSql(sql, map, orderQueryParams);

        // 執行queryForObject方法，並返回該過濾條件下的訂單總數
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return count;
    }

    private String addFilteringSql(String sql, Map<String, Object> map, OrderQueryParams orderQueryParams) {

        // 如果userId不為空，則添加過濾條件
        if (orderQueryParams.getUserId() != null) {
            sql += " AND user_id = :userId";
            map.put("userId", orderQueryParams.getUserId());
        }

        return sql;
    }
}
