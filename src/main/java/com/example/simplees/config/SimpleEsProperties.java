package com.example.simplees.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Simple-ES配置属性
 * 用于读取application.yml中的simple-es配置
 * application.yml → Spring Boot → SimpleEsProperties → 你的业务代码
 * @author Simple-ES
 */
@Data
@Component
@ConfigurationProperties(prefix = "simple-es")
public class SimpleEsProperties {
    
    /**
     * ES服务器地址
     */
    private String host = "localhost";
    
    /**
     * ES服务器端口
     */
    private int port = 9200;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 连接超时时间（毫秒）
     */
    private int connectTimeout = 5000;
    
    /**
     * 套接字超时时间（毫秒）
     */
    private int socketTimeout = 60000;
    
    /**
     * 是否启用DSL打印
     */
    private boolean printDsl = true;
    
    /**
     * 是否启用驼峰转下划线
     */
    private boolean mapUnderscoreToCamelCase = true;
}
