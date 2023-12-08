package ru.spb.nicetu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    static String metricPath;
    static int updateInterval = 30;
    public static void main(String[] args) {
        logger.info("Start service application");
       try {
           logger.info("Config application");
           FileInputStream configStream = new FileInputStream(args[0]);
           Properties props = new Properties();
           props.load(configStream);
           metricPath = props.getProperty("metricsPath");
           logger.info("Metric dir path configured to: {}", props.getProperty("metricPath"));
           updateInterval = Integer.parseInt(props.getProperty("updateInterval"));
           logger.info("Update interval set {} minutes", updateInterval);
           MonitoringService ms = new ImpMonitoringService();
           logger.info("Init monitoring service");
           ms.init();
       }
       catch (Exception e) {
           logger.error("Fatal error. Coused {}", e.getCause());
       }

    }
}