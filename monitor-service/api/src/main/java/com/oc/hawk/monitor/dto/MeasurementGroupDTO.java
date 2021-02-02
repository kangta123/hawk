package com.oc.hawk.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementGroupDTO {
    private List<MeasurementDTO> measurements;

    private String title;

    private String name;
    private String unit;

}
