package ru.spb.nicetu.profile.data;

import com.fasterxml.jackson.annotation.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect
public class MetricProtocol {
    @JsonProperty("date")
    private final String date;
    @JsonProperty
    private List<ProfilingData> records;

    public MetricProtocol() {
        ZonedDateTime currentDay = ZonedDateTime.now(ZoneOffset.UTC);
        date = currentDay.format(DateTimeFormatter.ofPattern("uuuu-MM-ddX"));
        records = new ArrayList<>();
    }

    public void addToProtocol(ProfilingData data) {
        records.add(data);
    }

    public String getDate() {
        return date;
    }
}
