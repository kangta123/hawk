package com.oc.hawk.monitor.application;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QueryMetricGroupParam {
    private List<Long> groupIds;
    private List<String> groupNames;
    private List<String> metrics;
}
