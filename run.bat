@echo off
setlocal
cd /d "%~dp0"
set CP=lib\mysql-connector-j-8.4.0.jar;lib\jbcrypt-0.4.jar;lib\slf4j-api-2.0.16.jar;lib\logback-classic-1.5.8.jar;lib\logback-core-1.5.8.jar;out
java -cp "%CP%" com.library.Main
