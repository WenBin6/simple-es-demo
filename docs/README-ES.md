# Simple-ES Demo Elasticsearch 使用指南

## 🚀 快速启动

### 启动 Elasticsearch

```bash
./start-es.sh
```

### 停止 Elasticsearch

```bash
./stop-es.sh
```

### 检查状态

```bash
./status-es.sh
```

## 📋 配置说明

### 连接信息

- **地址**: `localhost:9200`
- **认证**: 无（开发环境）
- **集群名**: `simple-es-demo-cluster`
- **节点名**: `simple-es-demo-node`

### 数据存储

- **数据目录**: `./elasticsearch-7.17.27/data/`
- **日志目录**: `./elasticsearch-7.17.27/logs/`

## 🔧 手动启动（如果需要）

如果脚本不工作，可以手动启动：

```bash
# 设置Java环境
export ES_JAVA_HOME=./elasticsearch-7.17.27/jdk.app/Contents/Home

# 启动Elasticsearch
./elasticsearch-7.17.27/bin/elasticsearch
```

## 🌐 访问地址

- **Elasticsearch**: http://localhost:9200
- **集群健康**: http://localhost:9200/_cluster/health
- **节点信息**: http://localhost:9200/_nodes

## 📝 注意事项

1. **Java版本**: Elasticsearch使用自带的Java 22，不影响您的项目Java 8
2. **端口占用**: 确保9200端口未被占用
3. **数据持久化**: 数据存储在项目目录下，删除项目会丢失数据
4. **开发环境**: 已关闭安全认证，仅用于开发测试

## 🛠️ 故障排除

### 端口被占用

```bash
# 查看端口占用
lsof -i :9200

# 杀死占用进程
kill -9 <PID>
```

### 权限问题

```bash
# 给脚本添加执行权限
chmod +x *.sh
```

### 数据目录问题

```bash
# 清理数据目录（会丢失所有数据）
rm -rf elasticsearch-7.17.27/data/*
```
