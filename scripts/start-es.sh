#!/bin/bash

# Simple-ES Demo Elasticsearch 启动脚本
# 自动设置Java环境并启动Elasticsearch

echo "🚀 启动 Simple-ES Demo Elasticsearch..."

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# 切换到项目根目录
cd "$SCRIPT_DIR/.."

echo "📁 工作目录: $(pwd)"

# 检查Elasticsearch目录是否存在
if [ ! -d "./elasticsearch-7.17.27" ]; then
    echo "❌ 错误: 找不到 elasticsearch-7.17.27 目录"
    echo "请确保在 Simple-ES-Demo 目录下运行此脚本"
    exit 1
fi

# 设置Elasticsearch Java环境
export ES_JAVA_HOME="$(pwd)/elasticsearch-7.17.27/jdk.app/Contents/Home"

# 检查Java环境
echo "📋 检查Java环境..."
if [ -f "$ES_JAVA_HOME/bin/java" ]; then
    echo "ES Java版本: $($ES_JAVA_HOME/bin/java -version 2>&1 | head -1)"
else
    echo "❌ 错误: 找不到Java环境"
    exit 1
fi

# 检查Elasticsearch可执行文件
if [ ! -f "./elasticsearch-7.17.27/bin/elasticsearch" ]; then
    echo "❌ 错误: 找不到 elasticsearch 可执行文件"
    exit 1
fi

# 启动Elasticsearch
echo "🔧 启动Elasticsearch服务..."
./elasticsearch-7.17.27/bin/elasticsearch

echo "✅ Elasticsearch启动完成！"
echo "🌐 访问地址: http://localhost:9200" 