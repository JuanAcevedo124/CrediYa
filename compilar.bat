@echo off
REM Compilar CrediYa (CMD)
if not exist out mkdir out
javac -cp "lib/mysql-connector-j.jar" -d out src\com\crediya\model\*.java src\com\crediya\util\*.java src\com\crediya\exception\*.java src\com\crediya\dao\*.java src\com\crediya\service\*.java src\com\crediya\app\*.java
if %errorlevel%==0 echo Compilado OK. Ejecuta run.bat
