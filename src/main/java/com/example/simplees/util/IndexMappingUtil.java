package com.example.simplees.util;

/**
 * 索引映射配置工具类
 * 提供常用的索引映射配置模板
 * 
 * @author Simple-ES
 */
public class IndexMappingUtil {

    /**
     * 获取用户索引的映射配置
     * 这个映射配置定义了User实体类在ES中的字段类型和属性
     * 
     * @return 用户索引映射配置（JSON字符串）
     */
    public static String getUserIndexMapping() {
        return "{"
            + "  \"properties\": {"
            + "    \"id\": {"
            + "      \"type\": \"keyword\","
            + "      \"index\": true"
            + "    },"
            + "    \"username\": {"
            + "      \"type\": \"text\","
            + "      \"analyzer\": \"standard\","
            + "      \"search_analyzer\": \"standard\","
            + "      \"fields\": {"
            + "        \"keyword\": {"
            + "          \"type\": \"keyword\","
            + "          \"ignore_above\": 256"
            + "        }"
            + "      }"
            + "    },"
            + "    \"email\": {"
            + "      \"type\": \"keyword\","
            + "      \"index\": true"
            + "    },"
            + "    \"age\": {"
            + "      \"type\": \"integer\","
            + "      \"index\": true"
            + "    },"
            + "    \"gender\": {"
            + "      \"type\": \"keyword\","
            + "      \"index\": true"
            + "    },"
            + "    \"city\": {"
            + "      \"type\": \"keyword\","
            + "      \"index\": true"
            + "    },"
            + "    \"bio\": {"
            + "      \"type\": \"text\","
            + "      \"analyzer\": \"standard\","
            + "      \"search_analyzer\": \"standard\""
            + "    },"
            + "    \"tags\": {"
            + "      \"type\": \"keyword\","
            + "      \"index\": true"
            + "    },"
            + "    \"createTime\": {"
            + "      \"type\": \"date\","
            + "      \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\""
            + "    },"
            + "    \"updateTime\": {"
            + "      \"type\": \"date\","
            + "      \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\""
            + "    },"
            + "    \"isActive\": {"
            + "      \"type\": \"boolean\","
            + "      \"index\": true"
            + "    },"
            + "    \"score\": {"
            + "      \"type\": \"double\","
            + "      \"index\": true"
            + "    }"
            + "  }"
            + "}";
    }

    /**
     * 获取文章索引的映射配置
     * 用于演示更复杂的映射配置
     * 
     * @return 文章索引映射配置（JSON字符串）
     */
    public static String getArticleIndexMapping() {
        return "{"
            + "  \"properties\": {"
            + "    \"id\": {"
            + "      \"type\": \"keyword\""
            + "    },"
            + "    \"title\": {"
            + "      \"type\": \"text\","
            + "      \"analyzer\": \"ik_max_word\","
            + "      \"search_analyzer\": \"ik_smart\","
            + "      \"fields\": {"
            + "        \"keyword\": {"
            + "          \"type\": \"keyword\","
            + "          \"ignore_above\": 256"
            + "        }"
            + "      }"
            + "    },"
            + "    \"content\": {"
            + "      \"type\": \"text\","
            + "      \"analyzer\": \"ik_max_word\","
            + "      \"search_analyzer\": \"ik_smart\""
            + "    },"
            + "    \"author\": {"
            + "      \"type\": \"keyword\""
            + "    },"
            + "    \"category\": {"
            + "      \"type\": \"keyword\""
            + "    },"
            + "    \"tags\": {"
            + "      \"type\": \"keyword\""
            + "    },"
            + "    \"publishTime\": {"
            + "      \"type\": \"date\","
            + "      \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\""
            + "    },"
            + "    \"viewCount\": {"
            + "      \"type\": \"long\""
            + "    },"
            + "    \"likeCount\": {"
            + "      \"type\": \"long\""
            + "    },"
            + "    \"isPublished\": {"
            + "      \"type\": \"boolean\""
            + "    }"
            + "  }"
            + "}";
    }

    /**
     * 获取商品索引的映射配置
     * 包含地理位置和嵌套对象
     * 
     * @return 商品索引映射配置（JSON字符串）
     */
    public static String getProductIndexMapping() {
        return "{"
            + "  \"properties\": {"
            + "    \"id\": {"
            + "      \"type\": \"keyword\""
            + "    },"
            + "    \"name\": {"
            + "      \"type\": \"text\","
            + "      \"analyzer\": \"ik_max_word\","
            + "      \"search_analyzer\": \"ik_smart\","
            + "      \"fields\": {"
            + "        \"keyword\": {"
            + "          \"type\": \"keyword\","
            + "          \"ignore_above\": 256"
            + "        }"
            + "      }"
            + "    },"
            + "    \"description\": {"
            + "      \"type\": \"text\","
            + "      \"analyzer\": \"ik_max_word\","
            + "      \"search_analyzer\": \"ik_smart\""
            + "    },"
            + "    \"price\": {"
            + "      \"type\": \"double\""
            + "    },"
            + "    \"category\": {"
            + "      \"type\": \"keyword\""
            + "    },"
            + "    \"brand\": {"
            + "      \"type\": \"keyword\""
            + "    },"
            + "    \"location\": {"
            + "      \"type\": \"geo_point\""
            + "    },"
            + "    \"attributes\": {"
            + "      \"type\": \"nested\","
            + "      \"properties\": {"
            + "        \"name\": {"
            + "          \"type\": \"keyword\""
            + "        },"
            + "        \"value\": {"
            + "          \"type\": \"text\""
            + "        }"
            + "      }"
            + "    },"
            + "    \"images\": {"
            + "      \"type\": \"keyword\""
            + "    },"
            + "    \"createTime\": {"
            + "      \"type\": \"date\","
            + "      \"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\""
            + "    }"
            + "  }"
            + "}";
    }

    /**
     * 获取简单的测试索引映射配置
     * 用于快速测试
     * 
     * @return 简单测试索引映射配置（JSON字符串）
     */
    public static String getSimpleTestMapping() {
        return "{"
            + "  \"properties\": {"
            + "    \"id\": {"
            + "      \"type\": \"keyword\""
            + "    },"
            + "    \"name\": {"
            + "      \"type\": \"text\""
            + "    },"
            + "    \"value\": {"
            + "      \"type\": \"integer\""
            + "    },"
            + "    \"timestamp\": {"
            + "      \"type\": \"date\","
            + "      \"format\": \"epoch_millis\""
            + "    }"
            + "  }"
            + "}";
    }

    /**
     * 获取索引设置配置
     * 包含分片数、副本数等设置
     * 
     * @param numberOfShards 主分片数
     * @param numberOfReplicas 副本数
     * @return 索引设置配置（JSON字符串）
     */
    public static String getIndexSettings(int numberOfShards, int numberOfReplicas) {
        return String.format("{"
            + "  \"number_of_shards\": %d,"
            + "  \"number_of_replicas\": %d,"
            + "  \"refresh_interval\": \"1s\","
            + "  \"max_result_window\": 10000"
            + "}", numberOfShards, numberOfReplicas);
    }

    /**
     * 获取默认索引设置配置
     * 
     * @return 默认索引设置配置（JSON字符串）
     */
    public static String getDefaultIndexSettings() {
        return getIndexSettings(1, 0); // 单节点环境：1个主分片，0个副本
    }
} 