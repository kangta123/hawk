package com.oc.hawk.monitor.domain.service;

import com.oc.hawk.monitor.domain.measurement.MeasurementGroupName;
import com.oc.hawk.monitor.domain.measurement.template.MeasurementTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * @author kangta123
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchMeasurementsTemplate {
    private MeasurementGroupName name;
    private String pod;
    private String app;
    private String version;
    private LocalDateTime start;
    private LocalDateTime end;
    private MeasurementTemplate template;

    public FetchMeasurementsTemplate withMeasurementTemplate(MeasurementTemplate template) {
        this.template = template;
        return this;
    }

    public long getEndUnixTimestamp() {
        return toUnixTimestamp(end);
    }

    public long getStartUnixTimestamp() {
        return toUnixTimestamp(start);
    }

    private long toUnixTimestamp(LocalDateTime from) {
        return from.toInstant(ZoneOffset.UTC).toEpochMilli() / 1000;
    }

    public long getDiffSecondBetweenStartAndEnd() {
        return start.until(end, ChronoUnit.SECONDS);
    }
}
