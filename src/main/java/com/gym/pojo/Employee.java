package com.gym.pojo;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeeId;
    private String employeeName;
    private String employeeGender;
    private Integer employeeAge;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate entryTime;
    private String staff;
    private String employeeMessage;
}