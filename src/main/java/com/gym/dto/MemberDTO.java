package com.gym.dto;

import java.io.Serializable;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
@Data
public class MemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer memberId;
    private String memberName;
    private String memberGender;
    private Integer memberAge;
    private Integer memberHeight;
    private Integer memberWeight;
    private String memberPhone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate cardTime;
    private Integer cardClass;
}

