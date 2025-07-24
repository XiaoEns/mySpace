@echo off
chcp 65001 >nul

:start

set /p port=   请输入要检查的端口号（输入0退出）：

:: 退出检测
if "%port%"=="0" exit
if "%port%"=="" (
    echo   输入不能为空！
    timeout /t 2 >nul
    goto start
)

:: 查找并显示进程
echo.
echo   端口 %port% 占用情况：
echo   ----------------------------------------------------------------------
netstat -ano | findstr ":%port% " >nul
if %errorlevel% equ 0 (
    netstat -ano | findstr ":%port% "
) else (
    echo   无进程占用该端口
)
echo   ----------------------------------------------------------------------

:: 如果有进程占用则提供终止选项
netstat -ano | findstr ":%port% " >nul
if %errorlevel% equ 0 (
    echo.
    set /p pid=   输入要终止的PID（输入0返回）：
    if "%pid%"=="0" goto start
    if "%pid%"=="" (
        echo   输入不能为空！
        timeout /t 2 >nul
        goto start
    )
    
    taskkill /f /pid %pid% >nul 2>&1 && (
        echo   已成功终止进程 %pid%
    ) || (
        echo   终止失败！请用管理员权限运行
    )
    timeout /t 3 >nul
) else (
    timeout /t 3 >nul
)
goto start