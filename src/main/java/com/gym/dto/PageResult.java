package com.gym.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private int total;  // 总记录数
    private List<T> list;  // 当前页数据

    public PageResult(int total, List<T> list) {
        this.total = total;
        this.list = list;
    }
}