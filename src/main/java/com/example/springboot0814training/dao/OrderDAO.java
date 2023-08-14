package com.example.springboot0814training.dao;

import com.example.springboot0814training.model.Order;
import com.example.springboot0814training.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDAO {
    Integer createOrder(Integer userId, BigDecimal totalAmount);

    void createOrderItem(Integer orderId, List<OrderItem> orderItemList);

    Order getOrderById(Integer orderId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
}
