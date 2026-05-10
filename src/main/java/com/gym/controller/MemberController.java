package com.gym.controller;

import com.gym.pojo.Member;
import com.gym.service.MemberService;
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

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam(required = false) String keyword) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Member> members;
            if (keyword != null && !keyword.trim().isEmpty()) {
                members = memberService.selectByKeyword(keyword);
            } else {
                members = memberService.selectAll();
            }
            result.put("success", true);
            result.put("data", members);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
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