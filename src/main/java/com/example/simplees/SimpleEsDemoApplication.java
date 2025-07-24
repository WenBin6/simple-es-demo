package com.example.simplees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Simple ES Demo 应用程序启动类
 * 
 * @author Simple ES Demo
 * @since 1.0.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.simplees",
    "org.example"
})
public class SimpleEsDemoApplication {

    /**
     * 应用程序入口方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动Spring Boot应用程序
        SpringApplication.run(SimpleEsDemoApplication.class, args);
        
        System.out.println("==========================================");
        System.out.println("🚀 Simple ES Demo 应用程序启动成功！");
        System.out.println("📝 这是一个简单的Elasticsearch操作演示项目");
        System.out.println("🔗 访问地址: http://localhost:8080");
        System.out.println("📊 健康检查: http://localhost:8080/actuator/health");
        System.out.println("==========================================");
    }
}