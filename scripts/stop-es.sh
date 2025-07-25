#!/bin/bash

# Simple-ES Demo Elasticsearch 停止脚本

echo "🛑 停止 Simple-ES Demo Elasticsearch..."

# 查找Elasticsearch进程
ES_PID=$(ps aux | grep elasticsearch | grep -v grep | awk '{print $2}')

if [ -z "$ES_PID" ]; then
    echo "❌ 未找到运行中的Elasticsearch进程"
else
    echo "📋 找到Elasticsearch进程 PID: $ES_PID"
    echo "🔧 正在停止Elasticsearch..."
    kill $ES_PID
    
    # 等待进程结束
    sleep 3
    
    # 检查是否还有进程
    if ps -p $ES_PID > /dev/null; then
        echo "⚠️  强制停止Elasticsearch..."
        kill -9 $ES_PID
    fi
    
    echo "✅ Elasticsearch已停止"
fi 