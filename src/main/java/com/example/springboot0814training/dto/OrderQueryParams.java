package com.example.springboot0814training.dto;

public class OrderQueryParams {

    private Integer userId;

    private Integer page;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
