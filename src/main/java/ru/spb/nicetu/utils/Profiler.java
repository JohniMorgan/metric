package ru.spb.nicetu.utils;

import ru.spb.nicetu.profile.data.ProfilingData;

public interface Profiler {
    void init();
    void setOnCounter(String key, Number number);
    void createCounter(String key);
    ProfilingData collectData(int period);
}
