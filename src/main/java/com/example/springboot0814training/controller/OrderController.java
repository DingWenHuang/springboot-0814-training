package com.example.springboot0814training.controller;

import com.example.springboot0814training.dto.CreateOrderRequest;
import com.example.springboot0814training.dto.OrderQueryParams;
import com.example.springboot0814training.model.Order;
import com.example.springboot0814training.service.OrderService;
import com.example.springboot0814training.util.Page;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest) {
        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(201).body(order);
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(@PathVariable Integer userId,
                                                 @RequestParam(defaultValue = "1") Integer page) {
        OrderQueryParams orderQueryParams = new OrderQueryParams();
        orderQueryParams.setUserId(userId);
        orderQueryParams.setPage(page);

        List<Order> orderList = orderService.getOrders(orderQueryParams);

        Integer total = orderService.countOrders(orderQueryParams);

        Page<Order> orderPage = new Page<>();

        orderPage.setPage(page);
        orderPage.setTotal(total);
        orderPage.setResults(orderList);



        return ResponseEntity.status(200).body(orderPage);
    }
}
