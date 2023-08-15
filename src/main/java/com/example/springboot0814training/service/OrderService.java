package com.example.springboot0814training.service;

import com.example.springboot0814training.dto.CreateOrderRequest;
import com.example.springboot0814training.dto.OrderQueryParams;
import com.example.springboot0814training.model.Order;

import java.util.List;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);

    List<Order> getOrders(OrderQueryParams orderQueryParams);

    Integer countOrders(OrderQueryParams orderQueryParams);
}
