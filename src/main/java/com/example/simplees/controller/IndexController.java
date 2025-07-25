package com.example.simplees.controller;

import com.example.simplees.service.IndexService;
import com.example.simplees.util.IndexMappingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 索引操作控制器
 * 提供索引的创建、删除、查询等REST API接口
 *
 * @author Simple-ES
 */
@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 创建用户索引
     */
    @PostMapping("/user")
    public Map<String, Object> createUserIndex() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始创建用户索引...");
            
            // 获取用户索引的映射配置
            String userMapping = IndexMappingUtil.getUserIndexMapping();
            
            // 创建索引
            boolean success = indexService.createIndex("user", userMapping);
            
            if (success) {
                result.put("success", true);
                result.put("message", "用户索引创建成功");
                result.put("indexName", "user");
                log.info("用户索引创建成功");
            } else {
                result.put("success", false);
                result.put("message", "用户索引创建失败");
                log.error("用户索引创建失败");
            }
            
        } catch (Exception e) {
            log.error("创建用户索引时发生异常", e);
            result.put("success", false);
            result.put("message", "创建用户索引时发生异常: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 创建文章索引
     */
    @PostMapping("/article")
    public Map<String, Object> createArticleIndex() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始创建文章索引...");
            
            // 获取文章索引的映射配置
            String articleMapping = IndexMappingUtil.getArticleIndexMapping();
            
            // 创建索引
            boolean success = indexService.createIndex("article", articleMapping);
            
            if (success) {
                result.put("success", true);
                result.put("message", "文章索引创建成功");
                result.put("indexName", "article");
                log.info("文章索引创建成功");
            } else {
                result.put("success", false);
                result.put("message", "文章索引创建失败");
                log.error("文章索引创建失败");
            }
            
        } catch (Exception e) {
            log.error("创建文章索引时发生异常", e);
            result.put("success", false);
            result.put("message", "创建文章索引时发生异常: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 创建简单测试索引
     */
    @PostMapping("/test")
    public Map<String, Object> createTestIndex() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始创建测试索引...");
            
            // 获取简单测试索引的映射配置
            String testMapping = IndexMappingUtil.getSimpleTestMapping();
            
            // 创建索引
            boolean success = indexService.createIndex("test", testMapping);
            
            if (success) {
                result.put("success", true);
                result.put("message", "测试索引创建成功");
                result.put("indexName", "test");
                log.info("测试索引创建成功");
            } else {
                result.put("success", false);
                result.put("message", "测试索引创建失败");
                log.error("测试索引创建失败");
            }
            
        } catch (Exception e) {
            log.error("创建测试索引时发生异常", e);
            result.put("success", false);
            result.put("message", "创建测试索引时发生异常: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 创建自定义索引
     */
    @PostMapping("/custom")
    public Map<String, Object> createCustomIndex(
            @RequestParam String indexName,
            @RequestParam(required = false) String mappings) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始创建自定义索引: {}", indexName);
            
            // 创建索引
            boolean success = indexService.createIndex(indexName, mappings);
            
            if (success) {
                result.put("success", true);
                result.put("message", "自定义索引创建成功");
                result.put("indexName", indexName);
                log.info("自定义索引 {} 创建成功", indexName);
            } else {
                result.put("success", false);
                result.put("message", "自定义索引创建失败");
                log.error("自定义索引 {} 创建失败", indexName);
            }
            
        } catch (Exception e) {
            log.error("创建自定义索引时发生异常", e);
            result.put("success", false);
            result.put("message", "创建自定义索引时发生异常: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除索引
     */
    @DeleteMapping("/{indexName}")
    public Map<String, Object> deleteIndex(@PathVariable String indexName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始删除索引: {}", indexName);
            
            boolean success = indexService.deleteIndex(indexName);
            
            if (success) {
                result.put("success", true);
                result.put("message", "索引删除成功");
                result.put("indexName", indexName);
                log.info("索引 {} 删除成功", indexName);
            } else {
                result.put("success", false);
                result.put("message", "索引删除失败");
                log.error("索引 {} 删除失败", indexName);
            }
            
        } catch (Exception e) {
            log.error("删除索引时发生异常", e);
            result.put("success", false);
            result.put("message", "删除索引时发生异常: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 检查索引是否存在
     */
    @GetMapping("/{indexName}/exists")
    public Map<String, Object> indexExists(@PathVariable String indexName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("检查索引是否存在: {}", indexName);
            
            boolean exists = indexService.indexExists(indexName);
            
            result.put("success", true);
            result.put("exists", exists);
            result.put("indexName", indexName);
            result.put("message", exists ? "索引存在" : "索引不存在");
            
            log.info("索引 {} 存在状态: {}", indexName, exists);
            
        } catch (Exception e) {
            log.error("检查索引是否存在时发生异常", e);
            result.put("success", false);
            result.put("message", "检查索引是否存在时发生异常: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取索引信息
     */
    @GetMapping("/{indexName}/info")
    public Map<String, Object> getIndexInfo(@PathVariable String indexName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("获取索引信息: {}", indexName);
            
            Map<String, Object> indexInfo = indexService.getIndexInfo(indexName);
            
            if (indexInfo != null) {
                result.put("success", true);
                result.put("indexName", indexName);
                result.put("data", indexInfo);
                result.put("message", "获取索引信息成功");
                log.info("索引 {} 信息获取成功", indexName);
            } else {
                result.put("success", false);
                result.put("message", "索引不存在或获取信息失败");
                log.error("索引 {} 信息获取失败", indexName);
            }
            
        } catch (Exception e) {
            log.error("获取索引信息时发生异常", e);
            result.put("success", false);
            result.put("message", "获取索引信息时发生异常: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取所有索引列表
     */
    @GetMapping("/list")
    public Map<String, Object> getAllIndices() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("获取所有索引列表");
            
            Map<String, Object> indices = indexService.getAllIndices();
            
            if (indices != null) {
                result.put("success", true);
                result.put("data", indices);
                result.put("message", "获取索引列表成功");
                log.info("索引列表获取成功");
            } else {
                result.put("success", false);
                result.put("message", "获取索引列表失败");
                log.error("索引列表获取失败");
            }
            
        } catch (Exception e) {
            log.error("获取索引列表时发生异常", e);
            result.put("success", false);
            result.put("message", "获取索引列表时发生异常: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 刷新索引
     */
    @PostMapping("/{indexName}/refresh")
    public Map<String, Object> refreshIndex(@PathVariable String indexName) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("刷新索引: {}", indexName);
            
            boolean success = indexService.refreshIndex(indexName);
            
            if (success) {
                result.put("success", true);
                result.put("message", "索引刷新成功");
                result.put("indexName", indexName);
                log.info("索引 {} 刷新成功", indexName);
            } else {
                result.put("success", false);
                result.put("message", "索引刷新失败");
                log.error("索引 {} 刷新失败", indexName);
            }
            
        } catch (Exception e) {
            log.error("刷新索引时发生异常", e);
            result.put("success", false);
            result.put("message", "刷新索引时发生异常: " + e.getMessage());
        }
        
        return result;
    }
} 