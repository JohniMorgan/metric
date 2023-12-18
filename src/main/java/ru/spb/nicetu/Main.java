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
        logger.info("Инициализация сервиса");

       try {
           logger.info("Конфигурация сервиса");
           FileInputStream configStream = new FileInputStream(args[0]);
           Properties props = new Properties();
           props.load(configStream);
           metricPath = props.getProperty("metricsPath");
           logger.info("Путь к файлам метрик сконфигурирован: {}", metricPath);
           updateInterval = Integer.parseInt(props.getProperty("updateInterval"));
           logger.info("Установлен интервал обновления данных {} минут", updateInterval);
           MonitoringService ms = new ImpMonitoringService();
           logger.info("Запрос инициализации сервиса мониторинга");
           ms.init();
           //Catch closing service moment and put msg into logs
           Runtime.getRuntime().addShutdownHook(new Thread(() -> logger.warn("Получен сигнал прерывания от системы. Сервис завершает работу")));
       }
       catch (Exception e) {
           logger.error("Фатальная ошибка. Причина: {}", e.toString());
       }
    }
}