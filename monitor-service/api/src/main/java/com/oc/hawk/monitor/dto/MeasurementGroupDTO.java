package com.oc.hawk.monitor.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
