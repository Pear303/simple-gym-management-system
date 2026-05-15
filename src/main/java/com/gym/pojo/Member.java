package com.gym.pojo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Member {
    private Integer memberId;
    private String memberName;
    private String memberGender;
    private Integer memberAge;
    private Integer memberHeight;
    private Integer memberweight;
    private String memberPhone;
    private LocalDate cardTime;
    private Integer cardClass;
    private Integer cardNextClass;
}