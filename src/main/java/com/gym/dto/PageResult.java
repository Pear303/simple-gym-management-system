package com.gym.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int total;
    private List<T> list;

    public PageResult(int total, List<T> list) {
        this.total = total;
        this.list = list;
    }
}