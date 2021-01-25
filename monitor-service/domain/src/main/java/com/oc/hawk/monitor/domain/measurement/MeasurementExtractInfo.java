package com.oc.hawk.monitor.domain.measurement;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class MeasurementExtractInfo {
    public static final int METRIC_POINT_INTERVAL = 10;
    private String namespace;
    private String app;
    private String version;
    private LocalDateTime start;
    private LocalDateTime end;

    public String getZonedStartTime() {
        return ZonedDateTime.of(start.minusSeconds(METRIC_POINT_INTERVAL), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public String getZonedEndTime() {
        return ZonedDateTime.of(end, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }


//
//    private boolean decideWithTags() {
//        long starTimeStamp = DateUtils.toLong(start);
//        long endTimeStamp = DateUtils.toLong(end);
//
//        return endTimeStamp - startTimeStamp >= DEGREE_COUNT * THRESHOLD_SECOND_TEN;
//    }

//    private String decideDegree() {
//        long startTimeStamp = DateUtils.toLong(start);
//        long endTimeStamp = DateUtils.toLong(end);
//        long timeStamp = endTimeStamp - startTimeStamp;
//        if (timeStamp <= DEGREE_COUNT * THRESHOLD_SECOND_TEN * 2) {
//            return "20s";
//        }
//        int degree = new BigDecimal(timeStamp)
//                .divide(new BigDecimal(1000), 0, RoundingMode.HALF_UP)
//                .divide(new BigDecimal(DEGREE_COUNT), 0, RoundingMode.HALF_UP)
//                .divide(new BigDecimal(10), 0, RoundingMode.HALF_UP)
//                .multiply(new BigDecimal(10))
//                .intValue();
//        return degree + "s";
//    }
}
