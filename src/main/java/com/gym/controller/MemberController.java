package com.gym.controller;

import com.gym.dto.MemberDTO;
import com.gym.dto.PageResult;
import com.gym.pojo.Member;
import com.gym.service.MemberService;
import com.gym.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/search/id/{id}")
    public ResponseEntity<Map<String, Object>> searchById(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Member member = memberService.selectById(id);
            if (member == null) {
                result.put("success", false);
                result.put("message", "会员不存在");
                return ResponseEntity.badRequest().body(result);
            }
            result.put("success", true);
            result.put("data", member);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @PostMapping("/search/regex")
    public ResponseEntity<Map<String, Object>> searchByRegex(@RequestBody Map<String, String> body) {
        String field = body.get("field");
        String value = body.get("value");
        System.out.println("[DEBUG] 收到请求: field=" + field + " | value=" + value + " | value长度=" + (value != null ? value.length() : 0));
        System.out.println("[DEBUG] value每个字符: " + (value != null ? java.util.Arrays.toString(value.toCharArray()) : "null"));
        try {
            List<Member> members = memberService.selectByRegex(field, value, null, null, null);
            return ResponseEntity.ok(ResponseUtil.success(members));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.error("查询失败: " + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "30") int pageSize) {
        try {
            PageResult<MemberDTO> pageResult = memberService.getMemberPage(pageNum, pageSize);
            return ResponseEntity.ok(ResponseUtil.success(pageResult));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ResponseUtil.error("查询失败: " + e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(@RequestBody Member member) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = memberService.insert(member);
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "添加成功");
            } else {
                result.put("success", false);
                result.put("message", "添加失败");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> update(@RequestBody Member member) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = memberService.update(member);
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "更新成功");
            } else {
                result.put("success", false);
                result.put("message", "更新失败");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer memberId) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = memberService.delete(memberId);
            if (rows > 0) {
                result.put("success", true);
                result.put("message", "删除成功");
            } else {
                result.put("success", false);
                result.put("message", "删除失败");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
}