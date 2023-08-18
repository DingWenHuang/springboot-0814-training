package com.example.springboot0814training.controller;

import com.example.springboot0814training.constant.ProductCategory;
import com.example.springboot0814training.dto.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    public ProductControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getProduct_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/1");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.productId", equalTo(1)))
                .andExpect(jsonPath("$.productName", equalTo("蘋果（澳洲）")))
                .andExpect(jsonPath("$.price", equalTo(30)))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.stock", equalTo(10)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Test
    public void getProduct_productIdNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/100");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(404));
    }

    @Test
    public void getProduct_productIdNotInteger() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/abc");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void createProduct_success() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("蘋果（澳洲）");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://example.com/apple.jpg");
        productRequest.setPrice(BigDecimal.valueOf(30));
        productRequest.setStock(10);
        productRequest.setDescription("澳洲進口蘋果，每顆約300g");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType("application/json")
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.productId", notNullValue()))
                .andExpect(jsonPath("$.productName", equalTo("蘋果（澳洲）")))
                .andExpect(jsonPath("$.price", equalTo(30)))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.stock", equalTo(10)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Test
    public void createProduct_illegalArgument() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .contentType("application/json")
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void updateProduct_success() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("http://test.com");
        productRequest.setPrice(BigDecimal.valueOf(100));
        productRequest.setStock(2);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/1")
                .contentType("application/json")
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.productId", equalTo(1)))
                .andExpect(jsonPath("$.productName", equalTo("test food product")))
                .andExpect(jsonPath("$.price", equalTo(100)))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.stock", equalTo(2)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Transactional
    @Test
    public void updateProduct_productIdNotFound() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("http://test.com");
        productRequest.setPrice(BigDecimal.valueOf(100));
        productRequest.setStock(2);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/100")
                .contentType("application/json")
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(404));
    }

    @Transactional
    @Test
    public void updateProduct_illegalArgument() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("test food product");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/1")
                .contentType("application/json")
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void deleteProduct_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/1");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(204));
    }

    @Transactional
    @Test
    public void deleteProduct_productIdNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/100");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(204));
    }

    @Test
    public void getProducts_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(7)))
                .andExpect(jsonPath("$.results", hasSize(5)))
                .andExpect(jsonPath("$.results[0].productId", equalTo(7)))
                .andExpect(jsonPath("$.results[0].productName", equalTo("Tesla")))
                .andExpect(jsonPath("$.results[0].price", equalTo(450000)))
                .andExpect(jsonPath("$.results[0].category", equalTo("CAR")))
                .andExpect(jsonPath("$.results[0].stock", equalTo(5)))
                .andExpect(jsonPath("$.results[0].createdDate", notNullValue()))
                .andExpect(jsonPath("$.results[0].lastModifiedDate", notNullValue()));
    }

    @Test
    public void getProducts_filtering() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("search", "蘋果")
                .param("category", "FOOD");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.results", hasSize(3)))
                .andExpect(jsonPath("$.results[0].productId", equalTo(3)))
                .andExpect(jsonPath("$.results[0].productName", equalTo("好吃又鮮甜的蘋果橘子")))
                .andExpect(jsonPath("$.results[0].price", equalTo(10)))
                .andExpect(jsonPath("$.results[0].category", equalTo("FOOD")))
                .andExpect(jsonPath("$.results[0].stock", equalTo(50)))
                .andExpect(jsonPath("$.results[0].createdDate", notNullValue()))
                .andExpect(jsonPath("$.results[0].lastModifiedDate", notNullValue()));
    }

    @Test
    public void getProducts_sorting() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                .param("orderBy", "price")
                .param("sort", "asc");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(7)))
                .andExpect(jsonPath("$.results", hasSize(5)))
                .andExpect(jsonPath("$.results[0].productId", equalTo(3)))
                .andExpect(jsonPath("$.results[0].productName", equalTo("好吃又鮮甜的蘋果橘子")))
                .andExpect(jsonPath("$.results[0].price", equalTo(10)))
                .andExpect(jsonPath("$.results[0].category", equalTo("FOOD")))
                .andExpect(jsonPath("$.results[0].stock", equalTo(50)))
                .andExpect(jsonPath("$.results[0].createdDate", notNullValue()))
                .andExpect(jsonPath("$.results[0].lastModifiedDate", notNullValue()));
    }

    @Test
    public void getProducts_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products")
                        .param("page", "2");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.page", equalTo(2)))
                .andExpect(jsonPath("$.total", equalTo(7)))
                .andExpect(jsonPath("$.results", hasSize(2)));
    }
}