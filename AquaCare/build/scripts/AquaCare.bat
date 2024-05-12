@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  AquaCare startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and AQUA_CARE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\AquaCare-1.0-SNAPSHOT.jar;%APP_HOME%\lib\influxdb-client-java-7.0.0.jar;%APP_HOME%\lib\paho-mqtt-client-1.14.2.jar;%APP_HOME%\lib\json-simple-1.1.1.jar;%APP_HOME%\lib\jfreechart-1.0.19.jar;%APP_HOME%\lib\jcommon-1.0.23.jar;%APP_HOME%\lib\influxdb-client-core-7.0.0.jar;%APP_HOME%\lib\influxdb-client-utils-7.0.0.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\joynr-mqtt-client-1.14.2.jar;%APP_HOME%\lib\messaging-common-1.14.2.jar;%APP_HOME%\lib\javaapi-1.14.2.jar;%APP_HOME%\lib\mqtt-client-0.0.6.jar;%APP_HOME%\lib\status-metrics-1.14.2.jar;%APP_HOME%\lib\guice-integration-1.14.2.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\junit-4.10.jar;%APP_HOME%\lib\guice-assistedinject-3.0.jar;%APP_HOME%\lib\guice-multibindings-3.0.jar;%APP_HOME%\lib\guice-3.0.jar;%APP_HOME%\lib\smrf-0.3.3.jar;%APP_HOME%\lib\hamcrest-core-1.1.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\cglib-2.2.1-v20090111.jar;%APP_HOME%\lib\smrf-api-0.3.3.jar;%APP_HOME%\lib\flatbuffers-java-1.10.0.jar;%APP_HOME%\lib\asm-3.1.jar;%APP_HOME%\lib\annotations-13.0.jar
set MODULE_PATH=%APP_HOME%\lib\javafx-fxml-21.0.1-win.jar;%APP_HOME%\lib\javafx-controls-21.0.1-win.jar;%APP_HOME%\lib\javafx-controls-21.0.1.jar;%APP_HOME%\lib\javafx-graphics-21.0.1-win.jar;%APP_HOME%\lib\javafx-graphics-21.0.1.jar;%APP_HOME%\lib\javafx-base-21.0.1-win.jar;%APP_HOME%\lib\javafx-base-21.0.1.jar;%APP_HOME%\lib\jSerialComm-2.11.0.jar;%APP_HOME%\lib\json-20240303.jar;%APP_HOME%\lib\rxjava-3.1.8.jar;%APP_HOME%\lib\adapter-rxjava3-2.9.0.jar;%APP_HOME%\lib\converter-scalars-2.9.0.jar;%APP_HOME%\lib\converter-gson-2.9.0.jar;%APP_HOME%\lib\retrofit-2.9.0.jar;%APP_HOME%\lib\okhttp-4.12.0.jar;%APP_HOME%\lib\logging-interceptor-4.12.0.jar;%APP_HOME%\lib\commons-csv-1.10.0.jar;%APP_HOME%\lib\gson-2.10.1.jar;%APP_HOME%\lib\reactive-streams-1.0.4.jar;%APP_HOME%\lib\jackson-databind-2.10.2.jar;%APP_HOME%\lib\okio-jvm-3.7.0.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.8.21.jar;%APP_HOME%\lib\kotlin-stdlib-1.9.21.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.8.21.jar;%APP_HOME%\lib\jackson-annotations-2.10.2.jar;%APP_HOME%\lib\jackson-core-2.10.2.jar

@rem Execute AquaCare
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %AQUA_CARE_OPTS%  -classpath "%CLASSPATH%" --module-path "%MODULE_PATH%" --module ui.aquacare/ui.aquacare.Main %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable AQUA_CARE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%AQUA_CARE_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
