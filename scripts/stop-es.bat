@echo off
REM Simple-ES Demo Elasticsearch 停止脚本 (Windows)

echo 🛑 停止 Simple-ES Demo Elasticsearch...

REM 查找Elasticsearch进程
for /f "tokens=2" %%i in ('tasklist /fi "imagename eq java.exe" /fo csv ^| findstr "elasticsearch"') do (
    set ES_PID=%%i
    goto :found
)

echo ❌ 未找到运行中的Elasticsearch进程
pause
exit /b 0

:found
echo 📋 找到Elasticsearch进程 PID: %ES_PID%
echo 🔧 正在停止Elasticsearch...

REM 尝试优雅停止
taskkill /PID %ES_PID% /F

REM 等待进程结束
timeout /t 3 /nobreak >nul

REM 检查是否还有进程
tasklist /FI "PID eq %ES_PID%" 2>NUL | find /I /N "%ES_PID%" >NUL
if "%ERRORLEVEL%"=="0" (
    echo ⚠️  强制停止Elasticsearch...
    taskkill /PID %ES_PID% /F
)

echo ✅ Elasticsearch已停止
pause 