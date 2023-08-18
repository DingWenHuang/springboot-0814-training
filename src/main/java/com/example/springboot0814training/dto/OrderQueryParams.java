package com.example.springboot0814training.dto;

public class OrderQueryParams {

    // OrderQueryParams是用來接收前端傳來的資料，用來查詢訂單
    // 需要提供使用者id、每頁顯示筆數、頁數

    private Integer userId;

    private Integer limit;

    private Integer page;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
