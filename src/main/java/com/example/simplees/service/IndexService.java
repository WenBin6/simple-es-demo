package com.example.simplees.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.PutIndicesSettingsResponse;
import co.elastic.clients.elasticsearch.indices.RefreshResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Elasticsearch索引操作服务
 * 提供索引的创建、删除、查询、更新等操作
 * 
 * @author Simple-ES
 */
@Slf4j
@Service
public class IndexService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    /**
     * 创建索引
     * 
     * @param indexName 索引名称
     * @return 是否创建成功
     */
    public boolean createIndex(String indexName) {
        return createIndex(indexName, null);
    }

    /**
     * 创建索引（带映射配置）
     * 
     * @param indexName 索引名称
     * @param mappings 映射配置（JSON字符串）
     * @return 是否创建成功
     */
    public boolean createIndex(String indexName, String mappings) {
        try {
            log.info("开始创建索引: {}", indexName);
            
            // 1. 检查索引是否已存在
            if (indexExists(indexName)) {
                log.warn("索引 {} 已存在，跳过创建", indexName);
                return true;
            }

            // 2. 创建索引请求构建器
            CreateIndexRequest.Builder requestBuilder = new CreateIndexRequest.Builder()
                .index(indexName);

            // 3. 如果提供了映射配置，则设置映射
            if (mappings != null && !mappings.trim().isEmpty()) {
                log.info("使用自定义映射配置创建索引");
                // 将JSON字符串转换为InputStream
                requestBuilder.mappings(m -> m.withJson(
                    new java.io.ByteArrayInputStream(mappings.getBytes(StandardCharsets.UTF_8))
                ));
            } else {
                log.info("使用默认映射配置创建索引");
                // 使用默认映射，ES会自动推断字段类型
            }

            // 4. 执行创建索引请求
            CreateIndexResponse response = elasticsearchClient.indices().create(requestBuilder.build());
            
            // 5. 检查响应结果
            boolean acknowledged = response.acknowledged();
            if (acknowledged) {
                log.info("索引 {} 创建成功", indexName);
            } else {
                log.error("索引 {} 创建失败", indexName);
            }
            
            return acknowledged;
            
        } catch (IOException e) {
            log.error("创建索引 {} 时发生异常", indexName, e);
            return false;
        }
    }

    /**
     * 检查索引是否存在
     * 
     * @param indexName 索引名称
     * @return 是否存在
     */
    public boolean indexExists(String indexName) {
        try {
            log.debug("检查索引是否存在: {}", indexName);
            
            // 使用exists API检查索引是否存在
            BooleanResponse response = elasticsearchClient.indices().exists(
                e -> e.index(indexName)
            );
            
            boolean exists = response.value();
            log.debug("索引 {} 存在状态: {}", indexName, exists);
            
            return exists;
            
        } catch (IOException e) {
            log.error("检查索引 {} 是否存在时发生异常", indexName, e);
            return false;
        }
    }

    /**
     * 删除索引
     * 
     * @param indexName 索引名称
     * @return 是否删除成功
     */
    public boolean deleteIndex(String indexName) {
        try {
            log.info("开始删除索引: {}", indexName);
            
            // 1. 检查索引是否存在
            if (!indexExists(indexName)) {
                log.warn("索引 {} 不存在，跳过删除", indexName);
                return true;
            }

            // 2. 执行删除索引请求
            DeleteIndexResponse response = elasticsearchClient.indices().delete(
                d -> d.index(indexName)
            );
            
            // 3. 检查响应结果
            boolean acknowledged = response.acknowledged();
            if (acknowledged) {
                log.info("索引 {} 删除成功", indexName);
            } else {
                log.error("索引 {} 删除失败", indexName);
            }
            
            return acknowledged;
            
        } catch (IOException e) {
            log.error("删除索引 {} 时发生异常", indexName, e);
            return false;
        }
    }

    /**
     * 获取索引信息
     * 
     * @param indexName 索引名称
     * @return 索引信息
     */
    public Map<String, Object> getIndexInfo(String indexName) {
        try {
            log.info("获取索引信息: {}", indexName);
            
            // 获取索引状态信息
            GetIndexResponse response = elasticsearchClient.indices().get(
                g -> g.index(indexName)
            );
            
            // 使用原始类型，避免类型转换问题
            Map<String, Object> indices = new HashMap<>();
            indices.put("indexInfo", response.result());
            log.info("索引 {} 信息获取成功", indexName);
            
            return indices;
            
        } catch (IOException e) {
            log.error("获取索引 {} 信息时发生异常", indexName, e);
            return null;
        }
    }

    /**
     * 获取所有索引列表
     * 
     * @return 索引列表
     */
    public Map<String, Object> getAllIndices() {
        try {
            log.info("获取所有索引列表");
            
            // 获取所有索引的状态信息
            GetIndexResponse response = elasticsearchClient.indices().get(
                g -> g.index("*")
            );
            
            // 使用原始类型，避免类型转换问题
            Map<String, Object> indices = new HashMap<>();
            indices.put("indices", response.result());
            log.info("获取到索引信息");
            
            return indices;
            
        } catch (IOException e) {
            log.error("获取所有索引列表时发生异常", e);
            return null;
        }
    }

    /**
     * 更新索引设置
     * 
     * @param indexName 索引名称
     * @param settings 设置配置（JSON字符串）
     * @return 是否更新成功
     */
    public boolean updateIndexSettings(String indexName, String settings) {
        try {
            log.info("更新索引设置: {}", indexName);
            
            // 1. 检查索引是否存在
            if (!indexExists(indexName)) {
                log.error("索引 {} 不存在，无法更新设置", indexName);
                return false;
            }

            // 2. 执行更新设置请求
            PutIndicesSettingsResponse response = elasticsearchClient.indices().putSettings(
                p -> p.index(indexName).withJson(
                    new java.io.ByteArrayInputStream(settings.getBytes(StandardCharsets.UTF_8))
                )
            );
            
            // 3. 检查响应结果
            boolean acknowledged = response.acknowledged();
            if (acknowledged) {
                log.info("索引 {} 设置更新成功", indexName);
            } else {
                log.error("索引 {} 设置更新失败", indexName);
            }
            
            return acknowledged;
            
        } catch (IOException e) {
            log.error("更新索引 {} 设置时发生异常", indexName, e);
            return false;
        }
    }

    /**
     * 刷新索引
     * 使索引中的更改立即可见
     * 
     * @param indexName 索引名称
     * @return 是否刷新成功
     */
    public boolean refreshIndex(String indexName) {
        try {
            log.info("刷新索引: {}", indexName);
            
            // 执行刷新请求
            RefreshResponse response = elasticsearchClient.indices().refresh(
                r -> r.index(indexName)
            );
            
            // RefreshResponse 没有 acknowledged() 方法，直接返回 true 表示成功
            log.info("索引 {} 刷新成功", indexName);
            return true;
            
        } catch (IOException e) {
            log.error("刷新索引 {} 时发生异常", indexName, e);
            return false;
        }
    }
} 