package ru.spb.nicetu.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spb.nicetu.profile.data.ProfilingData;

import java.util.HashMap;
import java.util.Map;

public class ImpSystemProfiler implements Profiler {
    private static Map<String, Number> systemParams;
    private static final Logger logger = LoggerFactory.getLogger(Profiler.class);

    @Override
    public void init() {
        logger.debug("Системный профилировщик инициализирован");
        systemParams = new HashMap<>();
    }
    @Override
    public void createCounter(String key) {
        systemParams.put(key, 0);
    }
    @Override
    public void setOnCounter(String key, Number number) throws IllegalArgumentException {
        if (systemParams.get(key) == null)
            throw new IllegalArgumentException("Counter named '" + key + "' doesn't exist");
        else {
            systemParams.put(key, number);
        }
    }
    public ProfilingData collectData(int period) {
        ProfilingData data = null;
        logger.debug("Сбор данный в строку для последующего превращения в JSON");
        try {
                data = new ProfilingData((int)Math.floor((double)systemParams.get("CPULoad")),
                    (Long) systemParams.get("totalPhysicalMemory"),
                    (Long) systemParams.get("freePhysicalMemory"),
                    (Long) systemParams.get("totalDriveSpace"),
                    (Long) systemParams.get("freeDriveSpace"), period);
        } catch (Exception e) {
            logger.error("Ошибка выполнения. Причина: {}", e.getMessage());
        }
        return data;
    }
}
