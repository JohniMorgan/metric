package ru.spb.nicetu;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.nicetu.profile.data.MetricProtocol;
import ru.spb.nicetu.utils.*;

import java.io.File;
import java.lang.management.ManagementFactory;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ImpMonitoringService implements MonitoringService {
    private static final Logger logger = LoggerFactory.getLogger(ImpMonitoringService.class);
    private final Profiler systemProfiler = new ImpSystemProfiler();
    private OperatingSystemMXBean systemBean;
    private final IJSONProvider provider = new JSONProvider();
    private MetricProtocol protocol;
    private IFileService fileService = new FileService(Main.metricPath);
    private boolean isStartState = true;
    private final File root = new File("/");
    private Runnable task = () -> collectTask();
    @Override
    public void init() {
        logger.info("Start service initialization");
        systemBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        systemProfiler.init();
        if (fileService.isExisted()) {
            logger.info("Data file was detected. Initialize MetricProtocol");
            protocol = (MetricProtocol) provider.parse(MetricProtocol.class, fileService);
        }
        else protocol = new MetricProtocol();
        try {
            logger.info("Initialization system profiler");
            systemProfiler.createCounter("CPULoad");
            systemProfiler.createCounter("totalPhysicalMemory");
            systemProfiler.createCounter("freePhysicalMemory");
            systemProfiler.createCounter("totalDriveSpace");
            systemProfiler.createCounter("freeDriveSpace");
        } catch (Exception e) {
            logger.error("Fatal error. System profiler error cause {}", e.getCause());
        }
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        //exec.scheduleWithFixedDelay(task, 0L, 30L, TimeUnit.SECONDS);
        exec.scheduleWithFixedDelay(task, 0L, Main.updateInterval, TimeUnit.MINUTES);
        logger.info("Service initialization success");
    }

    private void collectTask() {
        logger.info("Start execution task");
        if (!fileService.getTarget().getName().contains(
                ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("uuuu-MM-dd"))
        )) {
            logger.info("Detected new day. Refresh data file");
            fileService = new FileService(Main.metricPath);
        }
        double cpuLoad = systemBean.getSystemCpuLoad();
        if (cpuLoad < 0.0000000000001) {
            cpuLoad = 0.0;
        }
        try {
            logger.info("Getting metric data from system...");
            systemProfiler.setOnCounter("totalPhysicalMemory", systemBean.getTotalPhysicalMemorySize() / 1024 / 1024);
            systemProfiler.setOnCounter("freePhysicalMemory", systemBean.getFreePhysicalMemorySize() / 1024 / 1024);
            systemProfiler.setOnCounter("totalDriveSpace", root.getTotalSpace() / 1024 / 1024);
            systemProfiler.setOnCounter("freeDriveSpace", root.getFreeSpace() / 1024 / 1024);
            systemProfiler.setOnCounter("CPULoad", cpuLoad * 100);
            protocol.addToProtocol(systemProfiler.collectData(isStartState ? 0 : Main.updateInterval));
            fileService.write(provider.stringify(protocol));
            this.isStartState = false;
        } catch (Exception e) {
            logger.error("Task execution error cause {}", e.getMessage());
        }
    }
}
