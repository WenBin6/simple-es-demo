#!/bin/bash

# Simple-ES Demo Elasticsearch 状态检查脚本

echo "📊 检查 Simple-ES Demo Elasticsearch 状态..."

# 检查进程
ES_PID=$(ps aux | grep elasticsearch | grep -v grep | awk '{print $2}')

if [ -z "$ES_PID" ]; then
    echo "❌ Elasticsearch 未运行"
    exit 1
else
    echo "✅ Elasticsearch 正在运行 (PID: $ES_PID)"
fi

# 检查端口
if curl -s http://localhost:9200 > /dev/null; then
    echo "✅ Elasticsearch HTTP服务正常 (端口: 9200)"
    
    # 获取集群信息
    echo "📋 集群信息:"
    curl -s http://localhost:9200 | jq '.name, .cluster_name, .version.number' 2>/dev/null || curl -s http://localhost:9200
else
    echo "❌ Elasticsearch HTTP服务无响应"
fi 