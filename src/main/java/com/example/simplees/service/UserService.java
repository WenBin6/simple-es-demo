package com.example.simplees.service;

import com.example.simplees.entity.User;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务
 * 提供用户数据的增删改查操作
 *
 * @author Simple-ES
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private ElasticsearchClient client;

    private static final String INDEX_NAME = "user";

    /**
     * 创建单个用户
     */
    public User createUser(User user) {
        try {
            log.info("开始创建用户: {}", user.getUsername());

            // 设置默认值
            if (user.getId() == null) {
                user.setId(UUID.randomUUID().toString());
            }
            if (user.getCreateTime() == null) {
                user.setCreateTime(LocalDateTime.now());
            }
            user.setUpdateTime(LocalDateTime.now());
            if (user.getIsActive() == null) {
                user.setIsActive(true);
            }

            // 插入数据到ES
            IndexResponse response = client.index(i -> i
                .index(INDEX_NAME)
                .id(user.getId())
                .document(user)
            );

            if (response.result().name().equals("Created")) {
                log.info("用户创建成功: {}", user.getUsername());
                return user;
            } else {
                log.error("用户创建失败: {}", user.getUsername());
                throw new RuntimeException("用户创建失败");
            }

        } catch (IOException e) {
            log.error("创建用户时发生异常", e);
            throw new RuntimeException("创建用户失败: " + e.getMessage());
        }
    }

    /**
     * 批量创建用户
     */
    public List<User> createUsers(List<User> users) {
        try {
            log.info("开始批量创建用户，数量: {}", users.size());

            // 构建批量请求
            BulkRequest.Builder br = new BulkRequest.Builder();
            
            for (User user : users) {
                // 设置默认值
                if (user.getId() == null) {
                    user.setId(UUID.randomUUID().toString());
                }
                if (user.getCreateTime() == null) {
                    user.setCreateTime(LocalDateTime.now());
                }
                user.setUpdateTime(LocalDateTime.now());
                if (user.getIsActive() == null) {
                    user.setIsActive(true);
                }

                br.operations(op -> op
                    .index(idx -> idx
                        .index(INDEX_NAME)
                        .id(user.getId())
                        .document(user)
                    )
                );
            }

            // 执行批量插入
            BulkResponse response = client.bulk(br.build());

            // 检查结果
            if (response.errors()) {
                log.error("批量创建用户时发生错误");
                response.items().forEach(item -> {
                    if (item.error() != null) {
                        log.error("错误: {}", item.error().reason());
                    }
                });
                throw new RuntimeException("批量创建用户失败");
            }

            log.info("批量创建用户成功，数量: {}", users.size());
            return users;

        } catch (IOException e) {
            log.error("批量创建用户时发生异常", e);
            throw new RuntimeException("批量创建用户失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询用户
     */
    public User findById(String id) {
        try {
            log.debug("根据ID查询用户: {}", id);

            GetResponse<User> response = client.get(g -> g
                .index(INDEX_NAME)
                .id(id), User.class);

            if (response.found()) {
                User user = response.source();
                log.debug("找到用户: {}", user.getUsername());
                return user;
            } else {
                log.debug("用户不存在: {}", id);
                return null;
            }

        } catch (IOException e) {
            log.error("查询用户时发生异常", e);
            throw new RuntimeException("查询用户失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户名查询用户
     */
    public List<User> findByUsername(String username) {
        try {
            log.info("根据用户名查询用户: {}", username);

            SearchResponse<User> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(q -> q
                    .match(m -> m
                        .field("username")
                        .query(username)
                    )
                ), User.class);

            List<User> users = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

            log.info("找到 {} 个用户", users.size());
            return users;

        } catch (IOException e) {
            log.error("根据用户名查询用户时发生异常", e);
            throw new RuntimeException("查询用户失败: " + e.getMessage());
        }
    }

    /**
     * 根据邮箱精确查询用户
     */
    public User findByEmail(String email) {
        try {
            log.info("根据邮箱查询用户: {}", email);

            SearchResponse<User> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(q -> q
                    .term(t -> t
                        .field("email")
                        .value(email)
                    )
                ), User.class);

            List<User> users = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

            if (!users.isEmpty()) {
                log.info("找到用户: {}", users.get(0).getUsername());
                return users.get(0);
            } else {
                log.info("用户不存在: {}", email);
                return null;
            }

        } catch (IOException e) {
            log.error("根据邮箱查询用户时发生异常", e);
            throw new RuntimeException("查询用户失败: " + e.getMessage());
        }
    }

    /**
     * 复杂搜索用户
     */
    public Map<String, Object> searchUsers(String keyword, Integer minAge, Integer maxAge, 
                                          String gender, String city, Boolean isActive, 
                                          int page, int size) {
        try {
            log.info("开始复杂搜索用户，关键词: {}, 年龄范围: {}-{}, 性别: {}, 城市: {}", 
                    keyword, minAge, maxAge, gender, city);

            // 构建查询条件
            BoolQuery.Builder boolQuery = new BoolQuery.Builder();

            // 关键词搜索（用户名和简介）
            if (keyword != null && !keyword.trim().isEmpty()) {
                boolQuery.must(m -> m.multiMatch(mm -> mm
                    .query(keyword)
                    .fields("username", "bio")
                    .type(TextQueryType.BestFields)
                ));
            }

            // 年龄范围过滤
            if (minAge != null || maxAge != null) {
                RangeQuery.Builder rangeQuery = new RangeQuery.Builder().field("age");
                if (minAge != null) {
                    rangeQuery.gte(JsonData.of(minAge));
                }
                if (maxAge != null) {
                    rangeQuery.lte(JsonData.of(maxAge));
                }
                boolQuery.filter(f -> f.range(rangeQuery.build()));
            }

            // 性别过滤
            if (gender != null && !gender.trim().isEmpty()) {
                boolQuery.filter(f -> f.term(t -> t
                    .field("gender")
                    .value(gender)
                ));
            }

            // 城市过滤
            if (city != null && !city.trim().isEmpty()) {
                boolQuery.filter(f -> f.term(t -> t
                    .field("city")
                    .value(city)
                ));
            }

            // 激活状态过滤
            if (isActive != null) {
                boolQuery.filter(f -> f.term(t -> t
                    .field("isActive")
                    .value(isActive)
                ));
            }

            // 执行搜索
            SearchResponse<User> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(q -> q.bool(boolQuery.build()))
                .from((page - 1) * size)
                .size(size)
                .sort(sort -> sort
                    .field(f -> f
                        .field("createTime")
                        .order(SortOrder.Desc)
                    )
                ), User.class);

            // 处理结果
            List<User> users = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

            TotalHits totalHits = response.hits().total();
            long total = totalHits != null ? totalHits.value() : 0;

            Map<String, Object> result = new HashMap<>();
            result.put("users", users);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (total + size - 1) / size);

            log.info("搜索完成，找到 {} 个用户", total);
            return result;

        } catch (IOException e) {
            log.error("搜索用户时发生异常", e);
            throw new RuntimeException("搜索用户失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    public User updateUser(String id, User user) {
        try {
            log.info("开始更新用户: {}", id);

            // 设置更新时间
            user.setUpdateTime(LocalDateTime.now());

            // 更新用户
            UpdateResponse<User> response = client.update(u -> u
                .index(INDEX_NAME)
                .id(id)
                .doc(user), User.class);

            if (response.result().name().equals("Updated")) {
                log.info("用户更新成功: {}", id);
                return response.get().source();
            } else {
                log.error("用户更新失败: {}", id);
                throw new RuntimeException("用户更新失败");
            }

        } catch (IOException e) {
            log.error("更新用户时发生异常", e);
            throw new RuntimeException("更新用户失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(String id) {
        try {
            log.info("开始删除用户: {}", id);

            DeleteResponse response = client.delete(d -> d
                .index(INDEX_NAME)
                .id(id));

            if (response.result().name().equals("Deleted")) {
                log.info("用户删除成功: {}", id);
                return true;
            } else {
                log.error("用户删除失败: {}", id);
                return false;
            }

        } catch (IOException e) {
            log.error("删除用户时发生异常", e);
            throw new RuntimeException("删除用户失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        try {
            log.info("获取所有用户");

            SearchResponse<User> response = client.search(s -> s
                .index(INDEX_NAME)
                .query(q -> q.matchAll(m -> m))
                .size(1000), User.class);

            List<User> users = response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());

            log.info("获取到 {} 个用户", users.size());
            return users;

        } catch (IOException e) {
            log.error("获取所有用户时发生异常", e);
            throw new RuntimeException("获取用户列表失败: " + e.getMessage());
        }
    }

    /**
     * 统计用户数量
     */
    public long countUsers() {
        try {
            log.debug("统计用户数量");

            CountResponse response = client.count(c -> c
                .index(INDEX_NAME)
                .query(q -> q.matchAll(m -> m)));

            long count = response.count();
            log.debug("用户总数: {}", count);
            return count;

        } catch (IOException e) {
            log.error("统计用户数量时发生异常", e);
            throw new RuntimeException("统计用户数量失败: " + e.getMessage());
        }
    }
} 