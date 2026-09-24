@echo off
cd /d "%~dp0"

cd core
echo === [ Building core module... ] ===
call mvn clean install
IF %ERRORLEVEL% NEQ 0 (
    echo Core build failed. Exiting.
    exit /b %ERRORLEVEL%
)
cd ..

cd manhunt-mini
echo === [ Building manhunt-mini module... ] ===
call mvn clean package
IF %ERRORLEVEL% NEQ 0 (
    echo manhunt-mini build failed. Exiting.
    exit /b %ERRORLEVEL%
)
cd ..

echo === [ Build complete! ] ===
pause