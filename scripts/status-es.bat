@echo off
REM Simple-ES Demo Elasticsearch 状态检查脚本 (Windows)

echo 📊 检查 Simple-ES Demo Elasticsearch 状态...

REM 查找Elasticsearch进程
for /f "tokens=2" %%i in ('tasklist /fi "imagename eq java.exe" /fo csv ^| findstr "elasticsearch"') do (
    set ES_PID=%%i
    goto :found
)

echo ❌ Elasticsearch 未运行
pause
exit /b 1

:found
echo ✅ Elasticsearch 正在运行 (PID: %ES_PID%)

REM 检查端口
echo 📋 检查HTTP服务...
powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:9200' -TimeoutSec 5; if ($response.StatusCode -eq 200) { Write-Host '✅ Elasticsearch HTTP服务正常 (端口: 9200)'; Write-Host '📋 集群信息:'; $response.Content | ConvertFrom-Json | Select-Object name, cluster_name, @{Name='version';Expression={$_.version.number}} | ConvertTo-Json } else { Write-Host '❌ Elasticsearch HTTP服务异常' } } catch { Write-Host '❌ Elasticsearch HTTP服务无响应' }"

echo.
echo 📝 状态检查完成
pause 