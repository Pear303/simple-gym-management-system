package com.gym.service.impl;

import com.gym.mapper.EmployeeMapper;
import com.gym.pojo.Employee;
import com.gym.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public int selectTotalCount() {
        return employeeMapper.selectTotalCount();
    }

    @Override
    public List<Employee> selectAll() {
        return employeeMapper.selectAll();
    }

    @Override
    public Employee selectById(Integer employeeId) {
        return employeeMapper.selectById(employeeId);
    }

    @Override
    public List<Employee> selectByRegex(String field, String value, String operator, Integer numValue, String likePattern) {
        if (operator == null && numValue == null && likePattern == null) {
            ParseResult parsed = parseSearchValue(field, value);
            return employeeMapper.selectByRegex(field, parsed.operator, parsed.numValue, parsed.likePattern);
        }
        return employeeMapper.selectByRegex(field, operator, numValue, likePattern);
    }

    private ParseResult parseSearchValue(String field, String value) {
        ParseResult result = new ParseResult();

        if (value == null || value.isEmpty()) {
            return result;
        }

        boolean isNumericField = "employeeId".equals(field) || "employeeAge".equals(field);

        if (isNumericField) {
            System.out.println("[DEBUG parseSearchValue] 数值字段, value=" + value + " length=" + value.length());
            String op = null;
            String numStr = null;

            if (value.startsWith(">=") && value.length() > 2) {
                op = "ge";
                numStr = value.substring(2);
                System.out.println("[DEBUG parseSearchValue] 匹配 >= : op=" + op + " numStr=" + numStr);
            } else if (value.startsWith("<=") && value.length() > 2) {
                op = "le";
                numStr = value.substring(2);
                System.out.println("[DEBUG parseSearchValue] 匹配 <= : op=" + op + " numStr=" + numStr);
            } else if (value.startsWith(">") && value.length() > 1) {
                op = "gt";
                numStr = value.substring(1);
                System.out.println("[DEBUG parseSearchValue] 匹配 > : op=" + op + " numStr=" + numStr);
            } else if (value.startsWith("<") && value.length() > 1) {
                op = "lt";
                numStr = value.substring(1);
                System.out.println("[DEBUG parseSearchValue] 匹配 < : op=" + op + " numStr=" + numStr);
            } else {
                numStr = value;
                System.out.println("[DEBUG parseSearchValue] 无符号匹配, numStr=" + numStr);
            }

            if (numStr != null) {
                try {
                    result.numValue = Integer.parseInt(numStr.trim());
                    result.operator = op;
                    System.out.println("[DEBUG parseSearchValue] 解析成功: operator=" + op + " numValue=" + result.numValue);
                } catch (NumberFormatException e) {
                    System.out.println("[DEBUG parseSearchValue] 解析失败: numStr=" + numStr + " 错误=" + e.getMessage());
                }
            }
        } else {
            String cleanValue = value.replace("*", "%");
            if (cleanValue.startsWith("%") && cleanValue.endsWith("%") && cleanValue.length() > 2) {
                result.likePattern = cleanValue;
            } else if (cleanValue.startsWith("%") && cleanValue.length() > 1) {
                result.likePattern = cleanValue;
            } else if (cleanValue.endsWith("%") && cleanValue.length() > 1) {
                result.likePattern = cleanValue;
            } else {
                result.likePattern = "%" + cleanValue + "%";
            }
        }
        return result;
    }

    private static class ParseResult {
        String operator;
        Integer numValue;
        String likePattern;
    }

    @Override
    public int delete(Integer employeeId) {
        return employeeMapper.deleteById(employeeId);
    }

    @Override
    public int insert(Employee employee) {
        return employeeMapper.insert(employee);
    }

    @Override
    public int update(Employee employee) {
        return employeeMapper.update(employee);
    }

}