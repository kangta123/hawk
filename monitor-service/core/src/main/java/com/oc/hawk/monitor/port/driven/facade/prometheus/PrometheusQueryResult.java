package com.oc.hawk.monitor.port.driven.facade.prometheus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author kangta123
 */
enum PrometheusStatus {
    success, error
}

enum PrometheusResultType {
    matrix, vector, scalar, string
}

@Data
public class PrometheusQueryResult {
    private PrometheusStatus status;
    private PrometheusData data;

    public String[][] getValues() {
        if (isSuccess()) {
            List<PrometheusMetric> result = this.data.getResult();
            if (CollectionUtils.isEmpty(result)) {
                return null;
            }
            return result.iterator().next().getValues();
        } else {
            return null;
        }
    }

    public boolean isSuccess() {
        return status == PrometheusStatus.success;
    }
}

@Data
class PrometheusData {
    private PrometheusResultType resultType;
    private List<PrometheusMetric> result;
}

@Data
class PrometheusMetric {
    private Metric metric;
    private String[][] values;

    @Data
    static class Metric {
        /**
         * "metric": {
         * "__name__": "java_lang_Threading_CurrentThreadCpuTime",
         * "app": "CLIENT",
         * "id": "10.0.6.14:8080",
         * "instance": "10.0.6.14:9001",
         * "job": "jvm"
         * },
         */
        @JsonProperty("__name__")
        private String name;

        private String app;

        private String id;
        private String instance;
        private String job;
    }
}
