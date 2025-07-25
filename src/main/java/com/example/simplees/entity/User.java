package com.example.simplees.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类
 * 用于演示Elasticsearch索引操作
 * 
 * @author Simple-ES
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    /**
     * 用户ID - 将作为文档的唯一标识符
     * 注意：在ES中，如果不指定_id，ES会自动生成
     */
    private String id;
    
    /**
     * 用户名 - 用于全文搜索
     */
    private String username;
    
    /**
     * 邮箱 - 用于精确匹配
     */
    private String email;
    
    /**
     * 年龄 - 数值类型，用于范围查询
     */
    private Integer age;
    
    /**
     * 性别 - 枚举值，用于精确匹配
     */
    private String gender;
    
    /**
     * 城市 - 地理位置相关
     */
    private String city;
    
    /**
     * 个人简介 - 长文本，用于全文搜索
     */
    private String bio;
    
    /**
     * 标签列表 - 数组类型，用于多值匹配
     */
    private List<String> tags;
    
    /**
     * 创建时间 - 日期类型，用于时间范围查询
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间 - 日期类型
     */
    private LocalDateTime updateTime;
    
    /**
     * 是否激活 - 布尔类型，用于过滤
     */
    private Boolean isActive;
    
    /**
     * 积分 - 数值类型，用于排序和聚合
     */
    private Double score;
} 