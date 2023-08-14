package com.example.springboot0814training.service;

import com.example.springboot0814training.dto.CreateOrderRequest;
import com.example.springboot0814training.model.Order;

public interface OrderService {
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);
}
