package com.gym.controller;

import com.gym.pojo.Employee;
import com.gym.service.EmployeeService;
import com.gym.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Employee> employees = employeeService.selectAll();
            result.put("success", true);
            result.put("data", employees);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @GetMapping("/search/id/{id}")
    public ResponseEntity<Map<String, Object>> searchById(@PathVariable Integer id) {
        try {
            Employee employee = employeeService.selectById(id);
            if (employee == null || employee.getEmployeeId() == null) {
                return ResponseEntity.badRequest().body(ResponseUtil.error("员工不存在"));
            }
            return ResponseEntity.ok(ResponseUtil.success(employee));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.error("查询失败: " + e.getMessage()));
        }
    }

    @PostMapping("/search/regex")
    public ResponseEntity<Map<String, Object>> searchByRegex(@RequestBody Map<String, String> body) {
        String field = body.get("field");
        String value = body.get("value");
        System.out.println("[DEBUG] 收到请求: field=" + field + " | value=" + value + " | value长度=" + (value != null ? value.length() : 0));
        System.out.println("[DEBUG] value每个字符: " + (value != null ? java.util.Arrays.toString(value.toCharArray()) : "null"));
        try {
            List<Employee> employees = employeeService.selectByRegex(field, value, null, null, null);
            return ResponseEntity.ok(ResponseUtil.success(employees));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.error("查询失败: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Employee employee) {
        try {
            Employee existing = employeeService.selectById(employee.getEmployeeId());
            if (existing != null) {
                return ResponseEntity.badRequest().body(ResponseUtil.error("工号已存在"));
            }
            int rows = employeeService.insert(employee);
            if (rows > 0) {
                return ResponseEntity.ok(ResponseUtil.success("添加成功"));
            } else {
                return ResponseEntity.badRequest().body(ResponseUtil.error("添加失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.error("添加失败: " + e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody Employee employee) {
        try {
            int rows = employeeService.update(employee);
            if (rows > 0) {
                return ResponseEntity.ok(ResponseUtil.success("更新成功"));
            } else {
                return ResponseEntity.badRequest().body(ResponseUtil.error("更新失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.error("更新失败: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{employeeAccount}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer employeeAccount) {
        try {
            int rows = employeeService.delete(employeeAccount);
            if (rows > 0) {
                return ResponseEntity.ok(ResponseUtil.success("删除成功"));
            } else {
                return ResponseEntity.badRequest().body(ResponseUtil.error("删除失败"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.error("删除失败: " + e.getMessage()));
        }
    }

}