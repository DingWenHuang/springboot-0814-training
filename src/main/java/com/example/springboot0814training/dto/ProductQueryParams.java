package com.example.springboot0814training.dto;

import com.example.springboot0814training.constant.ProductCategory;

public class ProductQueryParams {

    // ProductQueryParams是用來接收前端傳來的資料，用來查詢商品
    // 需要提供搜尋字串、商品類別、排序欄位、排序方式、每頁顯示筆數、頁數
    // 若無提供則使用ProductController中的預設值

    private String search;

    private ProductCategory category;

    private String orderBy;

    private String sort;

    private Integer limit;

    private Integer page;

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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
