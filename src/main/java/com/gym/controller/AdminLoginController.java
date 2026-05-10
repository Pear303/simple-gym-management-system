package com.gym.controller;

import com.gym.pojo.Admin;
import com.gym.service.AdminService;
import com.gym.service.EmployeeService;
import com.gym.service.MemberService;
import com.gym.util.JwtUtil;
import com.gym.util.ResponseUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminLoginController {

    private final AdminService adminService;
    private final MemberService memberService;
    private final EmployeeService employeeService;

    public AdminLoginController(AdminService adminService,
                                MemberService memberService,
                                EmployeeService employeeService) {
        this.adminService = adminService;
        this.memberService = memberService;
        this.employeeService = employeeService;
    }

    @PostMapping("/admin-login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Admin admin) {
        if (admin == null || admin.getAdminAccount() == null || admin.getAdminPassword() == null) {
            return ResponseEntity.badRequest().body(ResponseUtil.error("请求参数不完整"));
        }
        try {
            Admin loggedIn = adminService.adminLogin(admin);
            if (loggedIn == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtil.unauthorized("账号或密码错误"));
            }

            // 此刻登录成功
            // 生成 JWT 风格的 token
            String token = JwtUtil.generateToken(loggedIn.getAdminId(), loggedIn.getAdminAccount());
                        
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("token", token);  // 封装 token 进去

            // 封装管理员信息，方便前端直接查询
            Map<String, Object> adminInfo = new HashMap<>();
            adminInfo.put("adminId", loggedIn.getAdminId());
            adminInfo.put("adminAccount", loggedIn.getAdminAccount());
            result.put("adminInfo", adminInfo);

            result.putAll(ResponseUtil.success());
                        
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.error("登录处理失败: " + e.getMessage()));
        }
    }

    @PostMapping("/admin-main-page")
    public ResponseEntity<Map<String, Object>> adminMainPage(HttpSession session) {
        Map<String, Object> mainPageData = new HashMap<>();
        mainPageData.put("success", true);
        mainPageData.put("memberTotalCount", session.getAttribute("memberTotalCount"));
        mainPageData.put("employeeTotalCount", session.getAttribute("employeeTotalCount"));
        mainPageData.put("peopleTotalCount", session.getAttribute("peopleTotalCount"));
        return ResponseEntity.ok(mainPageData);
    }

}