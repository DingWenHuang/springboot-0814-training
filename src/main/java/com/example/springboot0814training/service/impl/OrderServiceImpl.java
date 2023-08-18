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

        // 創建訂單前，先確認使用者是否存在
        User user = userDAO.getUserById(userId);

        // 如果使用者不存在，則回傳 404 NOT_FOUND，並且在 console 中印出警告訊息
        if (user == null) {
            log.warn("User ID: {} is not exists", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 從 createOrderRequest 中取得 buyItemList
        // 建立一個 orderItemList 來存放訂單項目
        List<BuyItem> buyItemList = createOrderRequest.getBuyItemList();
        List<OrderItem> orderItemList = new ArrayList<>();

        // 建立一個 BigDecimal 來存放訂單總金額
        BigDecimal totalAmount = new BigDecimal(0);

        // 逐一檢查 buyItemList 中的每一個 buyItem
        for (BuyItem buyItem : buyItemList) {

            // 檢查 buyItem 中的 productId 是否存在
            Product product = productDAO.getProductById(buyItem.getProductId());

            // 如果商品資料不存在，則回傳 400 BAD_REQUEST，並且在 console 中印出警告訊息
            if (product == null) {
                log.warn("Product ID: {} is not exists", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 檢查商品庫存是否足夠
            // 如果大於庫存，則回傳 400 BAD_REQUEST，並且在 console 中印出警告訊息
            if (product.getStock() < buyItem.getQuantity()) {
                log.warn("Product ID: {} is out of stock", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            // 更新庫存
            productDAO.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

            // 計算訂單項目的金額
            BigDecimal amount = product.getPrice().multiply(new BigDecimal(buyItem.getQuantity()));

            // 計算訂單總金額
            totalAmount = totalAmount.add(amount);

            // 建立訂單項目
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            // 將訂單項目加入 orderItemList
            orderItemList.add(orderItem);
        }

        // 建立訂單，並取得 orderId
        Integer orderId = orderDAO.createOrder(userId, totalAmount);

        // 根據 orderId 與 orderItemList 建立訂單項目
        orderDAO.createOrderItem(orderId, orderItemList);

        // 回傳 orderId
        return orderId;
    }

    @Override
    public Order getOrderById(Integer orderId) {

        // 根據 orderId 取得訂單
        Order order = orderDAO.getOrderById(orderId);

        // 根據 orderId 取得訂單項目
        List<OrderItem> orderItemList = orderDAO.getOrderItemsByOrderId(orderId);

        // 將訂單項目加入訂單後再回傳
        order.setOrderItemList(orderItemList);

        return order;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {

        // 先根據orderQueryParams中的userId取得使用者
        User user = userDAO.getUserById(orderQueryParams.getUserId());

        // 如果使用者不存在，則回傳 404 NOT_FOUND，並且在 console 中印出警告訊息
        if (user == null) {
            log.warn("User ID: {} is not exists", orderQueryParams.getUserId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // 根據 orderQueryParams 取得符合該條件的訂單
        List<Order> orderList = orderDAO.getOrders(orderQueryParams);

        // 取得orderList中每一筆訂單，並根據訂單id取得訂單項目
        // 將訂單項目加入訂單後再回傳
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderDAO.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }

    @Override
    public Integer countOrders(OrderQueryParams orderQueryParams) {

        // 根據 orderQueryParams 取得該條件下訂單數量
        return orderDAO.countOrders(orderQueryParams);
    }
}
