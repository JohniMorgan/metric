:: Требует для себа .jar и nssm.exe в дирректории запуска
:: Также необходимо запускать с правами администратора
:: Это нужно чтобы nssm смог смонтировать сервис
@echo off
set MY_CD=%CD%
set INSTALL_PATH=%SYSTEMDRIVE%\UnionPkMetrics
set PROPERTIES_FILE=%INSTALL_PATH%\pk_metrics.properties
set DATADIR_PATH=%SYSTEMDRIVE%\\UnionPkMetrics\\data
set SERVICE_NAME=UnionPkMetrics
MD %INSTALL_PATH%
MD %INSTALL_PATH%\service

:: Никак иначе файл создать у меня не получилось
echo metricsPath=%DATADIR_PATH%> %PROPERTIES_FILE%
echo updateInterval=30>> %PROPERTIES_FILE%

COPY %MY_CD%\metric-1.0-SNAPSHOT-jar-with-dependencies.jar %INSTALL_PATH%\service
COPY %MY_CD%\nssm.exe %INSTALL_PATH%\service

CD %INSTALL_PATH%\service

:: Создание непосредственно сервиса
nssm install %SERVICE_NAME% java -jar metric-1.0-SNAPSHOT-jar-with-dependencies.jar %PROPERTIES_FILE%
nssm set %SERVICE_NAME% AppDirectory "%CD%"
nssm set %SERVICE_NAME% AppStdout "%CD%\logs\stdout.log"
nssm set %SERVICE_NAME% AppStderr "%CD"\logs\stderr.log"