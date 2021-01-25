package com.oc.hawk.monitor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kangta123
 */
@Data
@NoArgsConstructor
public class MeasurementPointDTO {
    private Long date;
    private String value;

    public MeasurementPointDTO(Long date, String value) {
        this.date = date;
        this.value = value;
    }
}
