# Elasticsearch 安装配置指南

## 📋 目录

- [系统要求](#系统要求)
- [下载地址](#下载地址)
- [macOS 安装指南](#macos-安装指南)
- [Windows 安装指南](#windows-安装指南)
- [配置说明](#配置说明)
- [启动脚本](#启动脚本)
- [验证安装](#验证安装)
- [常见问题](#常见问题)

## 🖥️ 系统要求

### 最低要求

- **Java**: JDK 11 或更高版本
- **内存**: 至少 2GB RAM
- **磁盘空间**: 至少 1GB 可用空间

### 推荐配置

- **Java**: JDK 17 或更高版本
- **内存**: 4GB RAM 或更多
- **磁盘空间**: 5GB 可用空间

## 📥 下载地址

### 官方下载

- **Elasticsearch 7.17.27**: https://www.elastic.co/downloads/past-releases/elasticsearch-7-17-27
- **最新版本**: https://www.elastic.co/downloads/elasticsearch

### 选择版本说明

- **7.17.27**: 与当前项目兼容的稳定版本
- **包含JDK**: 选择包含JDK的版本，避免Java版本问题

## 🍎 macOS 安装指南

### 方法1：手动安装（推荐）

#### 步骤1：下载并解压

```bash
# 创建项目目录
cd /Users/tal/IdeaProjects/easy-es/Simple-ES-Demo

# 下载Elasticsearch（如果还没有）
# 从官网下载 elasticsearch-7.17.27-darwin-x86_64.tar.gz

# 解压到项目目录
tar -xzf elasticsearch-7.17.27-darwin-x86_64.tar.gz
mv elasticsearch-7.17.27-darwin-x86_64 elasticsearch-7.17.27
```

#### 步骤2：配置Elasticsearch

```bash
# 编辑配置文件
vim elasticsearch-7.17.27/config/elasticsearch.yml
```

将以下内容复制到配置文件中：

```yaml
# ======================== Elasticsearch Configuration =========================
# Simple-ES Demo 本地开发环境配置
# ======================== Elasticsearch Configuration =========================

# ---------------------------------- Cluster -----------------------------------
# 集群名称
cluster.name: simple-es-demo-cluster

# ------------------------------------ Node ------------------------------------
# 节点名称
node.name: simple-es-demo-node

# ----------------------------------- Paths ------------------------------------
# 数据存储路径
path.data: /Users/tal/IdeaProjects/easy-es/Simple-ES-Demo/elasticsearch-7.17.27/data

# 日志文件路径
path.logs: /Users/tal/IdeaProjects/easy-es/Simple-ES-Demo/elasticsearch-7.17.27/logs

# ----------------------------------- Memory -----------------------------------
# 内存锁定（开发环境可以关闭）
#bootstrap.memory_lock: true

# ---------------------------------- Network -----------------------------------
# 网络配置 - 只允许本地访问
network.host: localhost
http.port: 9200

# --------------------------------- Discovery ----------------------------------
# 单节点配置
discovery.type: single-node

# ---------------------------------- Various -----------------------------------
# 允许删除索引时使用通配符（开发环境）
action.destructive_requires_name: false

# ---------------------------------- Security ----------------------------------
# 开发环境关闭安全认证（生产环境请启用）
xpack.security.enabled: false

# ---------------------------------- 开发环境优化 ----------------------------------
# 允许跨域访问
http.cors.enabled: true
http.cors.allow-origin: "*"
http.cors.allow-methods: OPTIONS, HEAD, GET, POST, PUT, DELETE
http.cors.allow-headers: X-Requested-With,X-Auth-Token,Content-Type,Content-Length,Authorization

# 开发环境日志级别
logger.level: INFO
```

#### 步骤3：配置JVM内存

```bash
# 创建JVM配置目录
mkdir -p elasticsearch-7.17.27/config/jvm.options.d

# 创建开发环境JVM配置
cat > elasticsearch-7.17.27/config/jvm.options.d/development.options << EOF
# 设置堆内存大小（开发环境使用1GB）
-Xms1g
-Xmx1g

# 开发环境优化
-XX:+UnlockExperimentalVMOptions
-XX:+UseG1GC
-XX:G1HeapRegionSize=16m
-XX:G1ReservePercent=15
-XX:InitiatingHeapOccupancyPercent=35

# 开发环境性能优化
-XX:+OptimizeStringConcat
-XX:+UseStringDeduplication
EOF
```

### 方法2：使用Homebrew（可选）

```bash
# 安装Homebrew（如果没有）
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 安装Elasticsearch
brew tap elastic/tap
brew install elastic/tap/elasticsearch-full

# 启动服务
brew services start elasticsearch-full
```

## 🪟 Windows 安装指南

### 方法1：手动安装（推荐）

#### 步骤1：下载并解压

1. 从官网下载 `elasticsearch-7.17.27-windows-x86_64.zip`
2. 解压到项目目录：`C:\Users\YourName\IdeaProjects\easy-es\Simple-ES-Demo\`
3. 重命名文件夹为 `elasticsearch-7.17.27`

#### 步骤2：配置Elasticsearch

1. 编辑 `elasticsearch-7.17.27\config\elasticsearch.yml`
2. 将以下内容复制到配置文件中：

```yaml
# ======================== Elasticsearch Configuration =========================
# Simple-ES Demo 本地开发环境配置
# ======================== Elasticsearch Configuration =========================

# ---------------------------------- Cluster -----------------------------------
# 集群名称
cluster.name: simple-es-demo-cluster

# ------------------------------------ Node ------------------------------------
# 节点名称
node.name: simple-es-demo-node

# ----------------------------------- Paths ------------------------------------
# 数据存储路径（Windows路径格式）
path.data: C:/Users/YourName/IdeaProjects/easy-es/Simple-ES-Demo/elasticsearch-7.17.27/data

# 日志文件路径（Windows路径格式）
path.logs: C:/Users/YourName/IdeaProjects/easy-es/Simple-ES-Demo/elasticsearch-7.17.27/logs

# ----------------------------------- Memory -----------------------------------
# 内存锁定（Windows不支持，保持注释）
#bootstrap.memory_lock: true

# ---------------------------------- Network -----------------------------------
# 网络配置 - 只允许本地访问
network.host: localhost
http.port: 9200

# --------------------------------- Discovery ----------------------------------
# 单节点配置
discovery.type: single-node

# ---------------------------------- Various -----------------------------------
# 允许删除索引时使用通配符（开发环境）
action.destructive_requires_name: false

# ---------------------------------- Security ----------------------------------
# 开发环境关闭安全认证（生产环境请启用）
xpack.security.enabled: false

# ---------------------------------- 开发环境优化 ----------------------------------
# 允许跨域访问
http.cors.enabled: true
http.cors.allow-origin: "*"
http.cors.allow-methods: OPTIONS, HEAD, GET, POST, PUT, DELETE
http.cors.allow-headers: X-Requested-With,X-Auth-Token,Content-Type,Content-Length,Authorization

# 开发环境日志级别
logger.level: INFO
```

#### 步骤3：配置JVM内存

1. 创建目录：`elasticsearch-7.17.27\config\jvm.options.d\`
2. 创建文件：`elasticsearch-7.17.27\config\jvm.options.d\development.options`
3. 添加以下内容：

```
# 设置堆内存大小（开发环境使用1GB）
-Xms1g
-Xmx1g

# 开发环境优化
-XX:+UseG1GC
-XX:G1HeapRegionSize=16m
-XX:G1ReservePercent=15
-XX:InitiatingHeapOccupancyPercent=35

# 开发环境性能优化
-XX:+OptimizeStringConcat
-XX:+UseStringDeduplication
```

### 方法2：使用Chocolatey（可选）

```powershell
# 安装Chocolatey（如果没有）
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# 安装Elasticsearch
choco install elasticsearch

# 启动服务
Start-Service elasticsearch
```

## ⚙️ 配置说明

### 重要配置项

- **cluster.name**: 集群名称，用于标识
- **node.name**: 节点名称，用于标识
- **path.data**: 数据存储路径
- **path.logs**: 日志存储路径
- **network.host**: 网络绑定地址
- **http.port**: HTTP服务端口
- **discovery.type**: 发现类型，单节点使用single-node
- **xpack.security.enabled**: 安全认证开关

### 开发环境优化

- 关闭安全认证，简化开发
- 允许跨域访问，支持前端开发
- 设置合适的内存大小
- 配置日志级别

## 🚀 启动脚本

### macOS/Linux 启动脚本

```bash
#!/bin/bash
# 设置Java环境
export ES_JAVA_HOME=./elasticsearch-7.17.27/jdk.app/Contents/Home

# 启动Elasticsearch
./elasticsearch-7.17.27/bin/elasticsearch
```

### Windows 启动脚本

```batch
@echo off
REM 设置Java环境
set ES_JAVA_HOME=.\elasticsearch-7.17.27\jdk.app\Contents\Home

REM 启动Elasticsearch
.\elasticsearch-7.17.27\bin\elasticsearch.bat
```

## ✅ 验证安装

### 检查服务状态

```bash
# 检查进程
ps aux | grep elasticsearch

# 检查端口
lsof -i :9200

# 测试HTTP接口
curl -X GET "localhost:9200"
```

### 预期响应

```json
{
  "name" : "simple-es-demo-node",
  "cluster_name" : "simple-es-demo-cluster",
  "cluster_uuid" : "xxx",
  "version" : {
    "number" : "7.17.27",
    "build_flavor" : "default",
    "build_type" : "tar",
    "build_hash" : "xxx",
    "build_date" : "xxx",
    "build_snapshot" : false,
    "lucene_version" : "8.11.3",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

## 🔧 常见问题

### 1. Java版本问题

**问题**: `Future versions of Elasticsearch will require Java 11`
**解决**: 使用Elasticsearch自带的JDK，设置 `ES_JAVA_HOME`

### 2. 内存不足

**问题**: `Java heap space`
**解决**: 调整JVM内存设置，减少 `-Xms`和 `-Xmx`值

### 3. 端口被占用

**问题**: `Address already in use`
**解决**:

```bash
# 查找占用进程
lsof -i :9200
# 杀死进程
kill -9 <PID>
```

### 4. 权限问题

**问题**: `Permission denied`
**解决**:

```bash
# 给脚本添加执行权限
chmod +x elasticsearch-7.17.27/bin/elasticsearch
```

### 5. 数据目录问题

**问题**: `lock assertion failed`
**解决**:

```bash
# 清理数据目录（会丢失数据）
rm -rf elasticsearch-7.17.27/data/*
```

## 📝 注意事项

1. **路径配置**: 确保配置文件中的路径正确
2. **Java版本**: 推荐使用Elasticsearch自带的JDK
3. **内存设置**: 根据系统内存调整JVM设置
4. **安全考虑**: 开发环境关闭安全认证，生产环境必须启用
5. **数据备份**: 定期备份重要数据

## 🔗 相关链接

- [Elasticsearch官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/index.html)
- [Elasticsearch配置指南](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/settings.html)
- [JVM配置指南](https://www.elastic.co/guide/en/elasticsearch/reference/7.17/jvm-options.html)
