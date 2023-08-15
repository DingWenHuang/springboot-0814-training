package com.example.springboot0814training.service.impl;

import com.example.springboot0814training.dao.OrderDAO;
import com.example.springboot0814training.dao.ProductDAO;
import com.example.springboot0814training.dao.UserDAO;
import com.example.springboot0814training.dto.BuyItem;
import com.example.springboot0814training.dto.CreateOrderRequest;
import com.example.springboot0814training.dto.OrderQueryParams;
import com.example.springboot0814training.model.Order;
import com.example.springboot0814training.model.OrderItem;
import com.example.springboot0814training.model.Product;
import com.example.springboot0814training.model.User;
import com.example.springboot0814training.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderDAO orderDAO;

    private ProductDAO productDAO;

    private UserDAO userDAO;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderDAO orderDAO, ProductDAO productDAO, UserDAO userDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {

        User user = userDAO.getUserById(userId);

        if (user == null) {
            log.warn("User ID: {} is not exists", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<BuyItem> buyItemList = createOrderRequest.getBuyItemList();
        List<OrderItem> orderItemList = new ArrayList<>();

        BigDecimal totalAmount = new BigDecimal(0);

        for (BuyItem buyItem : buyItemList) {
            Product product = productDAO.getProductById(buyItem.getProductId());

            if (product == null) {
                log.warn("Product ID: {} is not exists", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            if (product.getStock() < buyItem.getQuantity()) {
                log.warn("Product ID: {} is out of stock", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            productDAO.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

            BigDecimal amount = product.getPrice().multiply(new BigDecimal(buyItem.getQuantity()));

            totalAmount = totalAmount.add(amount);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        Integer orderId = orderDAO.createOrder(userId, totalAmount);

        orderDAO.createOrderItem(orderId, orderItemList);


        return orderId;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDAO.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDAO.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {

        User user = userDAO.getUserById(orderQueryParams.getUserId());

        if (user == null) {
            log.warn("User ID: {} is not exists", orderQueryParams.getUserId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        List<Order> orderList = orderDAO.getOrders(orderQueryParams);

        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDAO.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }

    @Override
    public Integer countOrders(OrderQueryParams orderQueryParams) {
        return orderDAO.countOrders(orderQueryParams);
    }
}
