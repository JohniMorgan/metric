#!/bin/bash
# Script for Linux OS
# Required .jar file in current dir

INSTALL_PATH="/otp/nicetu/metrics/"
CONFIGURE_PATH="metricsPath=${INSTALL_PATH}/data"
CONFIGURE_INTERVAL="updateInterval=30"
SERVICE_PATH="/etc/systemd/system/pk_metrics.service"

#File variables
CONFIGURATION="${INSTALL_PATH}pk_metrics.properties"

#Check user rights
if [[ $EUID -ne 0 ]]; then
    echo "This script must be run as root"
    exit 1
fi

#Remove old version
if [[ -d $INSTALL_PATH ]]; then
    echo "---"
    rm -rfv $INSTALL_PATH
    rm  -v $CONFIGURATION
    rm -v $SERVICE_PATH
fi


# Creating directories
echo "----------------------"
mkdir -vp "${INSTALL_PATH}service/"
cp -v metric-1.0-SNAPSHOT-jar-with-dependencies.jar "${INSTALL_PATH}service/"
touch $CONFIGURATION
echo $CONFIGURE_PATH >> $CONFIGURATION
echo $CONFIGURE_INTERVAL >> $CONFIGURATION
echo "_---------------------"

#Creating service in systemd

touch $SERVICE_PATH
#Fill pk_metrics.service file
exec 1> $SERVICE_PATH
echo "[Unit]"
echo "Description=PK Metric service for source monitoring"
echo ""
echo "[Service]"
echo "ExecStart=java -jar ${INSTALL_PATH}service/metric-1.0-SNAPSHOT.jar ${CONFIGURATION}"
echo "ExecStop=/bin/kill -15 \${MAINPID}"
echo ""
echo "User=root"
echo "Group=root"
echo "Restart=always"
echo "RestartSec=10"
echo "[Install]"
echo "WantedBy=multi-user.target"

systemctl daemon-reload
systemctl enable pk_metrics.service


