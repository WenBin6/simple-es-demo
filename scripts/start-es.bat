@echo off
REM Simple-ES Demo Elasticsearch 启动脚本 (Windows)
REM 自动设置Java环境并启动Elasticsearch

echo 🚀 启动 Simple-ES Demo Elasticsearch...

REM 获取脚本所在目录
set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

echo 📁 工作目录: %CD%

REM 检查Elasticsearch目录是否存在
if not exist "elasticsearch-7.17.27" (
    echo ❌ 错误: 找不到 elasticsearch-7.17.27 目录
    echo 请确保在 Simple-ES-Demo 目录下运行此脚本
    pause
    exit /b 1
)

REM 设置Elasticsearch Java环境
set ES_JAVA_HOME=%CD%\elasticsearch-7.17.27\jdk.app\Contents\Home

REM 检查Java环境
echo 📋 检查Java环境...
if exist "%ES_JAVA_HOME%\bin\java.exe" (
    "%ES_JAVA_HOME%\bin\java.exe" -version 2>&1 | findstr "version"
) else (
    echo ❌ 错误: 找不到Java环境
    pause
    exit /b 1
)

REM 检查Elasticsearch可执行文件
if not exist "elasticsearch-7.17.27\bin\elasticsearch.bat" (
    echo ❌ 错误: 找不到 elasticsearch 可执行文件
    pause
    exit /b 1
)

REM 启动Elasticsearch
echo 🔧 启动Elasticsearch服务...
call elasticsearch-7.17.27\bin\elasticsearch.bat

echo ✅ Elasticsearch启动完成！
echo 🌐 访问地址: http://localhost:9200
pause 