package com.gym.dto;

import java.time.LocalDate;
import lombok.Data;
@Data
public class MemberDTO {
    private Integer memberId;
    private String memberName;
    private String memberGender;
    private Integer memberAge;
    private Integer memberHeight;
    private Integer memberWeight;
    private String memberPhone;
    private LocalDate cardTime;
    private Integer cardClass;
}

