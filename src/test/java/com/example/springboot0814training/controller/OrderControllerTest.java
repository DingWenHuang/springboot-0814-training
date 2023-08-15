package com.example.springboot0814training.controller;

import com.example.springboot0814training.dto.BuyItem;
import com.example.springboot0814training.dto.CreateOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public OrderControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @Transactional
    void createOrder_success() throws Exception {
        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(2);

        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(3);

        List<BuyItem> buyItemList = new ArrayList<>();
        buyItemList.add(buyItem1);
        buyItemList.add(buyItem2);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", notNullValue()))
                .andExpect(jsonPath("$.userId", equalTo(1)))
                .andExpect(jsonPath("$.totalAmount", equalTo(960)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()))
                .andExpect(jsonPath("$.orderItemList", hasSize(2)))
                .andExpect(jsonPath("$.orderItemList[0].orderItemId", notNullValue()))
                .andExpect(jsonPath("$.orderItemList[0].orderId", notNullValue()))
                .andExpect(jsonPath("$.orderItemList[0].productId", equalTo(1)))
                .andExpect(jsonPath("$.orderItemList[0].quantity", equalTo(2)))
                .andExpect(jsonPath("$.orderItemList[0].amount", equalTo(60)));

    }

    @Test
    @Transactional
    void createOrder_emptyBuyItemList() throws Exception {

        List<BuyItem> buyItemList = new ArrayList<>();

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    @Transactional
    void createOrder_userNotFound() throws Exception {
        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(2);

        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(3);

        List<BuyItem> buyItemList = new ArrayList<>();
        buyItemList.add(buyItem1);
        buyItemList.add(buyItem2);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/100/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(404));
    }

    @Test
    @Transactional
    void createOrder_productNotFound() throws Exception {
        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(100);
        buyItem1.setQuantity(2);

        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(3);

        List<BuyItem> buyItemList = new ArrayList<>();
        buyItemList.add(buyItem1);
        buyItemList.add(buyItem2);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    @Transactional
    void createOrder_insufficientStock() throws Exception {
        BuyItem buyItem1 = new BuyItem();
        buyItem1.setProductId(1);
        buyItem1.setQuantity(200);

        BuyItem buyItem2 = new BuyItem();
        buyItem2.setProductId(2);
        buyItem2.setQuantity(300);

        List<BuyItem> buyItemList = new ArrayList<>();
        buyItemList.add(buyItem1);
        buyItemList.add(buyItem2);

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setBuyItemList(buyItemList);

        String json = objectMapper.writeValueAsString(createOrderRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void gerOrders_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/1/orders");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(2)))
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].orderId", equalTo(2)))
                .andExpect(jsonPath("$.results[0].userId", equalTo(1)))
                .andExpect(jsonPath("$.results[0].totalAmount", equalTo(100000)))
                .andExpect(jsonPath("$.results[0].createdDate", notNullValue()))
                .andExpect(jsonPath("$.results[0].lastModifiedDate", notNullValue()))
                .andExpect(jsonPath("$.results[0].orderItemList", hasSize(1)))
                .andExpect(jsonPath("$.results[0].orderItemList[0].orderItemId", notNullValue()))
                .andExpect(jsonPath("$.results[0].orderItemList[0].orderId", equalTo(2)))
                .andExpect(jsonPath("$.results[0].orderItemList[0].productId", equalTo(4)))
                .andExpect(jsonPath("$.results[0].orderItemList[0].quantity", equalTo(1)))
                .andExpect(jsonPath("$.results[0].orderItemList[0].amount", equalTo(100000)));
    }

    @Test
    public void gerOrders_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/1/orders")
                        .param("page", "2");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.page", equalTo(2)))
                .andExpect(jsonPath("$.total", equalTo(2)))
                .andExpect(jsonPath("$.results", hasSize(0)));
    }

    @Test
    public void gerOrders_userHasNoOrder() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/2/orders");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(0)))
                .andExpect(jsonPath("$.results", hasSize(0)));
    }

    @Test
    public void gerOrders_userNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/999/orders");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(404));
    }
}