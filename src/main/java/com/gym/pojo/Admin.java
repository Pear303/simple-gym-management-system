package com.gym.pojo;

/*
 * pojo是实体类层，数据模型对象
 * 应数据库中的管理员表
 */

import lombok.Data;

@Data
public class Admin {
    private Long adminId;
    private String adminAccount;
    private String adminPassword;
}
