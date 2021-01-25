package com.oc.hawk.container.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMetricDTO {
    private String time;
    private String runnerCount;
    private String configurationCount;

}
