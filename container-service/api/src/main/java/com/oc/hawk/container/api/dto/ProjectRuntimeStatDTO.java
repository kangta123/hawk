package com.oc.hawk.container.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRuntimeStatDTO {
    private Map<Long, Integer> projectRunnerCountMap;
    private Map<Long, Integer> projectConfigurationCountMap;

}
