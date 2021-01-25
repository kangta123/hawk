package com.oc.hawk.monitor.domain.measurement;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class PodMetric {
    public static final String UNDER_LINE = "_";

    private LocalDateTime date;

    private double value;
//
//
//    public static PodMetric of(String color, LocalDateTime date, double value) {
//        PodMetric podMetric = new PodMetric();
//        podMetric.setColor(color);
//        podMetric.setDate(date);
//        podMetric.setValue(value);
//        return podMetric;
//    }

    private LocalDateTime getLocalDateTimeFromTimestamp(String time) {
        Instant from = Instant.from(Instant.ofEpochMilli(Long.parseLong(time)));
        return LocalDateTime.ofInstant(from, ZoneId.systemDefault());
    }


    public void setStrDate(String d) {
        date = getLocalDateTimeFromTimestamp(d);
    }


    public void setStrValue(String v) {
        value = Double.parseDouble(v);
    }
}
