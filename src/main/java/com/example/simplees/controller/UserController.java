package com.example.simplees.controller;

import com.example.simplees.entity.User;
import com.example.simplees.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户控制器
 * 提供用户数据的REST API接口
 *
 * @author Simple-ES
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 创建单个用户
     */
    @PostMapping
    public Map<String, Object> createUser(@RequestBody Map<String, Object> userData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始创建用户，接收到的数据: {}", userData);
            
            // 手动构建User对象，避免Jackson序列化问题
            User user = User.builder()
                .id(UUID.randomUUID().toString())
                .username((String) userData.get("username"))
                .email((String) userData.get("email"))
                .age(userData.get("age") != null ? Integer.valueOf(userData.get("age").toString()) : null)
                .gender((String) userData.get("gender"))
                .city((String) userData.get("city"))
                .bio((String) userData.get("bio"))
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isActive(true)
                .score(userData.get("score") != null ? Double.valueOf(userData.get("score").toString()) : null)
                .build();
            
            User createdUser = userService.createUser(user);
            
            result.put("success", true);
            result.put("message", "用户创建成功");
            result.put("data", createdUser);
            
        } catch (Exception e) {
            log.error("创建用户时发生异常", e);
            result.put("success", false);
            result.put("message", "创建用户失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 批量创建用户
     */
    @PostMapping("/batch")
    public Map<String, Object> createUsers(@RequestBody List<Map<String, Object>> usersData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始批量创建用户，数量: {}", usersData.size());
            
            List<User> users = usersData.stream().map(userData -> {
                return User.builder()
                    .id(UUID.randomUUID().toString())
                    .username((String) userData.get("username"))
                    .email((String) userData.get("email"))
                    .age(userData.get("age") != null ? Integer.valueOf(userData.get("age").toString()) : null)
                    .gender((String) userData.get("gender"))
                    .city((String) userData.get("city"))
                    .bio((String) userData.get("bio"))
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .isActive(true)
                    .score(userData.get("score") != null ? Double.valueOf(userData.get("score").toString()) : null)
                    .build();
            }).collect(java.util.stream.Collectors.toList());
            
            List<User> createdUsers = userService.createUsers(users);
            
            result.put("success", true);
            result.put("message", "批量创建用户成功");
            result.put("data", createdUsers);
            result.put("count", createdUsers.size());
            
        } catch (Exception e) {
            log.error("批量创建用户时发生异常", e);
            result.put("success", false);
            result.put("message", "批量创建用户失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("根据ID查询用户: {}", id);
            
            User user = userService.findById(id);
            
            if (user != null) {
                result.put("success", true);
                result.put("message", "查询用户成功");
                result.put("data", user);
            } else {
                result.put("success", false);
                result.put("message", "用户不存在");
            }
            
        } catch (Exception e) {
            log.error("查询用户时发生异常", e);
            result.put("success", false);
            result.put("message", "查询用户失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 根据用户名查询用户
     */
    @GetMapping("/search/username")
    public Map<String, Object> getUsersByUsername(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("根据用户名查询用户: {}", username);
            
            List<User> users = userService.findByUsername(username);
            
            result.put("success", true);
            result.put("message", "查询用户成功");
            result.put("data", users);
            result.put("count", users.size());
            
        } catch (Exception e) {
            log.error("根据用户名查询用户时发生异常", e);
            result.put("success", false);
            result.put("message", "查询用户失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 根据邮箱查询用户
     */
    @GetMapping("/search/email")
    public Map<String, Object> getUserByEmail(@RequestParam String email) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("根据邮箱查询用户: {}", email);
            
            User user = userService.findByEmail(email);
            
            if (user != null) {
                result.put("success", true);
                result.put("message", "查询用户成功");
                result.put("data", user);
            } else {
                result.put("success", false);
                result.put("message", "用户不存在");
            }
            
        } catch (Exception e) {
            log.error("根据邮箱查询用户时发生异常", e);
            result.put("success", false);
            result.put("message", "查询用户失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 复杂搜索用户
     */
    @GetMapping("/search")
    public Map<String, Object> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始复杂搜索用户");
            
            Map<String, Object> searchResult = userService.searchUsers(
                keyword, minAge, maxAge, gender, city, isActive, page, size);
            
            result.put("success", true);
            result.put("message", "搜索用户成功");
            result.putAll(searchResult);
            
        } catch (Exception e) {
            log.error("搜索用户时发生异常", e);
            result.put("success", false);
            result.put("message", "搜索用户失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateUser(@PathVariable String id, @RequestBody Map<String, Object> userData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始更新用户: {}", id);
            
            // 先查询现有用户
            User existingUser = userService.findById(id);
            if (existingUser == null) {
                result.put("success", false);
                result.put("message", "用户不存在");
                return result;
            }
            
            // 更新字段
            if (userData.get("username") != null) {
                existingUser.setUsername((String) userData.get("username"));
            }
            if (userData.get("email") != null) {
                existingUser.setEmail((String) userData.get("email"));
            }
            if (userData.get("age") != null) {
                existingUser.setAge(Integer.valueOf(userData.get("age").toString()));
            }
            if (userData.get("gender") != null) {
                existingUser.setGender((String) userData.get("gender"));
            }
            if (userData.get("city") != null) {
                existingUser.setCity((String) userData.get("city"));
            }
            if (userData.get("bio") != null) {
                existingUser.setBio((String) userData.get("bio"));
            }
            if (userData.get("score") != null) {
                existingUser.setScore(Double.valueOf(userData.get("score").toString()));
            }
            
            User updatedUser = userService.updateUser(id, existingUser);
            
            result.put("success", true);
            result.put("message", "用户更新成功");
            result.put("data", updatedUser);
            
        } catch (Exception e) {
            log.error("更新用户时发生异常", e);
            result.put("success", false);
            result.put("message", "更新用户失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable String id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("开始删除用户: {}", id);
            
            boolean success = userService.deleteUser(id);
            
            if (success) {
                result.put("success", true);
                result.put("message", "用户删除成功");
            } else {
                result.put("success", false);
                result.put("message", "用户删除失败");
            }
            
        } catch (Exception e) {
            log.error("删除用户时发生异常", e);
            result.put("success", false);
            result.put("message", "删除用户失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 获取所有用户
     */
    @GetMapping("/list")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("获取所有用户");
            
            List<User> users = userService.getAllUsers();
            
            result.put("success", true);
            result.put("message", "获取用户列表成功");
            result.put("data", users);
            result.put("count", users.size());
            
        } catch (Exception e) {
            log.error("获取所有用户时发生异常", e);
            result.put("success", false);
            result.put("message", "获取用户列表失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 统计用户数量
     */
    @GetMapping("/count")
    public Map<String, Object> countUsers() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("统计用户数量");
            
            long count = userService.countUsers();
            
            result.put("success", true);
            result.put("message", "统计用户数量成功");
            result.put("count", count);
            
        } catch (Exception e) {
            log.error("统计用户数量时发生异常", e);
            result.put("success", false);
            result.put("message", "统计用户数量失败: " + e.getMessage());
        }
        
        return result;
    }
}