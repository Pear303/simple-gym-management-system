package com.gym.service;

import com.gym.pojo.Employee;

import java.util.List;

public interface EmployeeService {

    int selectTotalCount();

    List<Employee> selectAll();

    Employee selectById(Integer employeeId);

    List<Employee> selectByRegex(String field, String value, String operator, Integer numValue, String likePattern);

    int insert(Employee employee);

    int update(Employee employee);

    int delete(Integer employeeId);
}