package com.oc.hawk.monitor.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MeasurementDTO {

    private String name;

    private String unit;
    private String title;

    private List<MeasurementPointDTO> data = new ArrayList<>();

}
