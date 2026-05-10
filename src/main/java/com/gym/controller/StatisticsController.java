package com.gym.controller;

import com.gym.service.EmployeeService;
import com.gym.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api")
public class StatisticsController {

    private final EmployeeService employeeService;
    private final MemberService memberService;

    public StatisticsController(EmployeeService employeeService, MemberService memberService) {
        this.employeeService = employeeService;
        this.memberService = memberService;
    }

    @GetMapping("/member/total-count")
    public ResponseEntity<Map<String, Object>> memberTotalCount() {
        return countQuery(() -> memberService.selectTotalCount(), "会员");
    }

    @GetMapping("/employee/total-count")
    public ResponseEntity<Map<String, Object>> employeeTotalCount() {
        return countQuery(() -> employeeService.selectTotalCount(), "员工");
    }

    private ResponseEntity<Map<String, Object>> countQuery(Supplier<Integer> countSupplier, String entityName) {
        try {
            Integer totalCount = countSupplier.get();
            if (totalCount == null || totalCount < 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(error(entityName + "总数异常"));
            }
            Map<String, Object> result = singleSuccess();
            result.put("totalCount", totalCount);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error("查询" + entityName + "总数失败: " + e.getMessage()));
        }
    }

    private static Map<String, Object> singleSuccess() {
        Map<String, Object> m = new HashMap<>(2);
        m.put("success", true);
        return m;
    }

    private static Map<String, Object> error(String message) {
        Map<String, Object> m = new HashMap<>(4);
        m.put("success", false);
        m.put("message", message);
        return m;
    }
}