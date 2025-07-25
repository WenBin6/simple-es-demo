package com.example.simplees.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import com.example.simplees.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于验证ES连接和基本功能
 *
 * @author Simple-ES
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    /**
     * 测试ES连接
     */
    @GetMapping("/es-connection")
    public Map<String, Object> testEsConnection() {
        Map<String, Object> result = new HashMap<>();

        try {
            log.info("开始测试ES连接...");

            // 获取集群健康状态
            HealthResponse healthResponse = elasticsearchClient.cluster().health();

            result.put("success", true);
            result.put("message", "ES连接成功");
            result.put("clusterName", healthResponse.clusterName());
            result.put("status", healthResponse.status().toString());
            result.put("numberOfNodes", healthResponse.numberOfNodes());
            result.put("activeShards", healthResponse.activeShards());

            log.info("ES连接测试成功: {}", result);

        } catch (Exception e) {
            log.error("ES连接测试失败", e);
            result.put("success", false);
            result.put("message", "ES连接失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 应用健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("message", "Simple-ES Demo 应用运行正常");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 测试用户创建（调试用）
     */
    @PostMapping("/create-user")
    public Map<String, Object> testCreateUser(@RequestBody Map<String, Object> userData) {
        Map<String, Object> result = new HashMap<>();

        try {
            log.info("测试创建用户，接收到的数据: {}", userData);

            // 手动构建User对象
            User user = User.builder()
                .username((String) userData.get("username"))
                .email((String) userData.get("email"))
                .age(userData.get("age") != null ? Integer.valueOf(userData.get("age").toString()) : null)
                .gender((String) userData.get("gender"))
                .city((String) userData.get("city"))
                .bio((String) userData.get("bio"))
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isActive(true)
                .build();

            log.info("构建的User对象: {}", user);

            result.put("success", true);
            result.put("message", "用户对象构建成功");
            result.put("user", user);

        } catch (Exception e) {
            log.error("测试创建用户时发生异常", e);
            result.put("success", false);
            result.put("message", "测试创建用户失败: " + e.getMessage());
        }

        return result;
    }
} 