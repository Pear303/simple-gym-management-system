package com.gym.service.impl;

import com.gym.dto.PageResult;
import com.gym.mapper.EmployeeMapper;
import com.gym.pojo.Employee;
import com.gym.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    @Cacheable(value = "employeeCount", unless = "#result <= 0")
    public int selectTotalCount() {
        return employeeMapper.selectTotalCount();
    }

    @Override
    @Cacheable(value = "employee:detail", key = "#employeeId", unless = "#result == null")
    public Employee selectById(Integer employeeId) {
        return employeeMapper.selectById(employeeId);
    }

    @Override
    public List<Employee> selectAll() {
        return employeeMapper.selectAll();
    }

    @Override
    public List<Employee> selectByRegex(String field, String value, String operator, Integer numValue, String likePattern) {
        if (operator == null && numValue == null && likePattern == null) {
            ParseResult parsed = parseSearchValue(field, value);
            return employeeMapper.selectByRegex(field, parsed.operator, parsed.numValue, parsed.likePattern, parsed.startDate, parsed.endDate);
        }
        return employeeMapper.selectByRegex(field, operator, numValue, likePattern, null, null);
    }

    @Override
    @Cacheable(value = "employee:page", key = "#pageNum + '-' + #pageSize")
    public PageResult<Employee> getEmployeePage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Employee> list = employeeMapper.selectPage(offset, pageSize);
        int total = employeeMapper.selectTotalCount();
        return new PageResult<>(total, list);
    }

    private ParseResult parseSearchValue(String field, String value) {
        ParseResult result = new ParseResult();

        if (value == null || value.isEmpty()) {
            return result;
        }

        if ("entryTime".equals(field)) {
            return parseDateSearchValue(value);
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

    private ParseResult parseDateSearchValue(String value) {
        ParseResult result = new ParseResult();
        System.out.println("[DEBUG parseDateSearchValue] 处理日期: " + value);

        // 方案一：字符串模式（复用现有逻辑）
        if (value.contains("*")) {
            String cleanValue = value.replace("*", "%");
            String normalized = normalizeDateLikePattern(cleanValue);
            result.likePattern = normalized;
            System.out.println("[DEBUG parseDateSearchValue] 字符串模式: " + result.likePattern);
            return result;
        }

        if (value.toLowerCase().startsWith("between ")) {
            return parseDateBetween(value);
        }

        if (value.matches("^[><=]+.*")) {
            return parseDateComparison(value);
        }

        if (value.matches("^\\d{4}$")) {
            String startDate = value + "-01-01";
            String endDate = (Integer.parseInt(value) + 1) + "-01-01";
            result.startDate = startDate;
            result.endDate = endDate;
            System.out.println("[DEBUG parseDateSearchValue] 年份: start=" + startDate + " end=" + endDate);
            return result;
        }

        if (value.matches("^\\d{4}-\\d{1,2}$")) {
            String normalized = normalizeDate(value + "-01");
            String startDate = normalized.substring(0, 8);
            String endDate = getNextMonthFirstDay(normalized.substring(0, 8));
            result.startDate = startDate;
            result.endDate = endDate;
            System.out.println("[DEBUG parseDateSearchValue] 年月: start=" + startDate + " end=" + endDate);
            return result;
        }

        if (value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            String normalized = normalizeDate(value);
            result.likePattern = normalized;
            System.out.println("[DEBUG parseDateSearchValue] 精确日期: " + normalized);
            return result;
        }

        result.likePattern = "%" + value + "%";
        System.out.println("[DEBUG parseDateSearchValue] 默认字符串: " + result.likePattern);
        return result;
    }

    private String normalizeDate(String date) {
        String[] parts = date.split("-");
        return String.format("%04d-%02d-%02d",
            Integer.parseInt(parts[0]),
            Integer.parseInt(parts[1]),
            parts.length > 2 ? Integer.parseInt(parts[2]) : 1);
    }

    private String normalizeDateLikePattern(String pattern) {
        return pattern
            .replaceAll("(?<=[-%])(\\d)(?=[-%]|$)", "0$1")
            .replaceAll("^(\\d)(?=[-%]|$)", "0$1");
    }

    private ParseResult parseDateBetween(String value) {
        ParseResult result = new ParseResult();
        String[] parts = value.substring(8).trim().split(",");
        if (parts.length != 2) {
            System.out.println("[DEBUG parseDateBetween] 格式错误");
            return result;
        }

        String start = parts[0].trim();
        String end = parts[1].trim();

        if (start.matches("^\\d{4}-\\d{1,2}$")) {
            start = normalizeDate(start + "-01").substring(0, 8);
        }
        if (end.matches("^\\d{4}-\\d{1,2}$")) {
            String normalized = normalizeDate(end + "-01").substring(0, 8);
            end = getLastDayOfMonth(normalized);
        }

        result.startDate = start;
        result.endDate = end;
        System.out.println("[DEBUG parseDateBetween] start=" + start + " end=" + end);
        return result;
    }

    private ParseResult parseDateComparison(String value) {
        ParseResult result = new ParseResult();

        if (value.startsWith(">=")) {
            result.startDate = normalizeDate(value.substring(2).trim());
            System.out.println("[DEBUG parseDateComparison] >= : " + result.startDate);
        } else if (value.startsWith("<=")) {
            result.endDate = normalizeDate(value.substring(2).trim());
            System.out.println("[DEBUG parseDateComparison] <= : " + result.endDate);
        } else if (value.startsWith(">")) {
            String raw = normalizeDate(value.substring(1).trim());
            result.startDate = getNextDay(raw);
            System.out.println("[DEBUG parseDateComparison] > : start=" + result.startDate);
        } else if (value.startsWith("<")) {
            String raw = normalizeDate(value.substring(1).trim());
            result.endDate = getPreviousDay(raw);
            System.out.println("[DEBUG parseDateComparison] < : end=" + result.endDate);
        }

        return result;
    }

    private String getNextMonthFirstDay(String date) {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        month++;
        if (month > 12) {
            month = 1;
            year++;
        }

        return String.format("%04d-%02d-01", year, month);
    }

    private String getLastDayOfMonth(String date) {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);

        int lastDay;
        if (month == 2) {
            lastDay = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0) ? 29 : 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            lastDay = 30;
        } else {
            lastDay = 31;
        }

        return String.format("%04d-%02d-%02d", year, month, lastDay);
    }

    private String getNextDay(String date) {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        day++;
        String maxDay = getLastDayOfMonth(date).split("-")[2];
        if (day > Integer.parseInt(maxDay)) {
            day = 1;
            month++;
            if (month > 12) {
                month = 1;
                year++;
            }
        }

        return String.format("%04d-%02d-%02d", year, month, day);
    }

    private String getPreviousDay(String date) {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        day--;
        if (day < 1) {
            month--;
            if (month < 1) {
                month = 12;
                year--;
            }
            String maxDay = getLastDayOfMonth(String.format("%04d-%02d-01", year, month)).split("-")[2];
            day = Integer.parseInt(maxDay);
        }

        return String.format("%04d-%02d-%02d", year, month, day);
    }

    private static class ParseResult {
        String operator;
        Integer numValue;
        String likePattern;
        String startDate;
        String endDate;
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "employeeCount", allEntries = true),
        @CacheEvict(value = "employee:page", allEntries = true)
    })
    public int delete(Integer employeeId) {
        return employeeMapper.deleteById(employeeId);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "employeeCount", allEntries = true),
        @CacheEvict(value = "employee:page", allEntries = true)
    })
    public int insert(Employee employee) {
        return employeeMapper.insert(employee);
    }

    @Override
    @Caching(evict = {
        @CacheEvict(value = "employee:detail", key = "#employee.employeeId"),
        @CacheEvict(value = "employeeCount", allEntries = true),
        @CacheEvict(value = "employee:page", allEntries = true)
    })
    public int update(Employee employee) {
        return employeeMapper.update(employee);
    }

}