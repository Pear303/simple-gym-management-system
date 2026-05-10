package com.gym.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一响应结果工具类
 */
public class ResponseUtil {

    /**
     * 成功响应
     */
    public static Map<String, Object> success() {
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        return result;
    }

    /**
     * 成功响应（带数据）
     */
    public static Map<String, Object> success(Object data) {
        Map<String, Object> result = new HashMap<>(4);
        result.put("success", true);
        result.put("data", data);
        return result;
    }

    /**
     * 未授权响应（401）
     */
    public static Map<String, Object> unauthorized(String message) {
        Map<String, Object> result = new HashMap<>(4);
        result.put("success", false);
        result.put("message", message);
        return result;
    }

    /**
     * 错误响应（400/500）
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> result = new HashMap<>(4);
        result.put("success", false);
        result.put("message", message);
        return result;
    }
}
