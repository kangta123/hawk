package com.oc.hawk.monitor.dto;

import lombok.Data;

@Data
public class MetricDTO {

    private Long id;

    private String title;

    private String metric;

    private String template;

    private String groupName;

    private Long groupId;

    private boolean enable;
}
