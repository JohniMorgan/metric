package ru.spb.nicetu.profile.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@JsonAutoDetect
public class ProfilingData {
    @JsonProperty("date")
    private String date;
    @JsonProperty("cpuLoad")
    private int cpuLoad;
    @JsonProperty("totalPhysicalMemory")
    private long totalMemory;
    @JsonProperty("freePhysicalMemory")
    private long freeMemory;
    @JsonProperty("totalDriveSpace")
    private long totalDrive;
    @JsonProperty("freeDriveSpace")
    private long freeDrive;
    @JsonProperty
    private int period;

    private ProfilingData() {}
    public ProfilingData(int cpu, long totalMemory, long freeMemory, long totalDrive, long freeDrive, int period) {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
        this.date = currentTime.format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX"));
        this.cpuLoad = cpu;
        this.totalMemory = totalMemory;
        this.freeMemory = freeMemory;
        this.totalDrive = totalDrive;
        this.freeDrive = freeDrive;
        this.period = period;
    }

    @Override
    public String toString() {
        return "{" +
                "\n   date: " + this.date +
                "\n   cpuLoad: " + this.cpuLoad +
                ",\n   totalPhysicalMemory: " + this.totalMemory +
                ",\n   freePhysicalMemory: " + this.freeMemory +
                ",\n   totalDriveSpace: " + this.totalDrive +
                ",\n   freeDriveSpace: " + this.freeDrive +
                "\n}";
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
