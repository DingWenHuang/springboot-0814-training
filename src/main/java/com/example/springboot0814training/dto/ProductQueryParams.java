package com.example.springboot0814training.dto;

import com.example.springboot0814training.constant.ProductCategory;

public class ProductQueryParams {

    private String search;

    private ProductCategory category;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }
}
