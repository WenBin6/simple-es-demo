package com.example.simplees.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Elasticsearch客户端配置
 * 负责创建和配置ElasticsearchClient
 * 
 * @author Simple-ES
 */
@Slf4j
@Configuration
public class ElasticsearchConfig {

    @Autowired
    private SimpleEsProperties properties;

    /**
     * 创建ElasticsearchClient Bean
     * 
     * @return ElasticsearchClient实例
     */
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        log.info("开始配置Elasticsearch客户端...");
        log.info("ES服务器地址: {}:{}", properties.getHost(), properties.getPort());
        
        try {
            // 1. 创建RestClient
            RestClient restClient = createRestClient();
            
            // 2. 创建Transport
            ElasticsearchTransport transport = new RestClientTransport(
                restClient, 
                new JacksonJsonpMapper()
            );
            
            // 3. 创建ElasticsearchClient
            ElasticsearchClient client = new ElasticsearchClient(transport);
            
            log.info("Elasticsearch客户端配置成功");
            return client;
            
        } catch (Exception e) {
            log.error("Elasticsearch客户端配置失败", e);
            throw new RuntimeException("ES客户端配置失败", e);
        }
    }

    /**
     * 创建RestClient
     * 
     * @return RestClient实例
     */
    private RestClient createRestClient() {
        // 创建HttpHost
        HttpHost httpHost = new HttpHost(properties.getHost(), properties.getPort(), "http");
        
        // 创建RestClientBuilder
        RestClientBuilder builder = RestClient.builder(httpHost);

        // 配置连接超时
        builder.setRequestConfigCallback(requestConfigBuilder -> 
            requestConfigBuilder
                .setConnectTimeout(properties.getConnectTimeout())
                .setSocketTimeout(properties.getSocketTimeout())
        );

        // 配置认证（如果提供了用户名和密码）
        if (properties.getUsername() != null && !properties.getUsername().isEmpty()) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword())
            );
            
            builder.setHttpClientConfigCallback(httpClientBuilder -> 
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            );
            
            log.info("已配置ES认证: 用户名={}", properties.getUsername());
        }

        return builder.build();
    }
}
