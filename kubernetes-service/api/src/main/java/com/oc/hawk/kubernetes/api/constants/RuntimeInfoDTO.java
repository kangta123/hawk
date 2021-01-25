package com.oc.hawk.kubernetes.api.constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Data
public class RuntimeInfoDTO {
    private String id;
    private String podName;
    private String name;
    private String serviceName;
    private String image;
    private String version;
    private String projectId;
    private String namespace;
    private String status;
    private Integer restartCount;
    private Long startTime;
    private String nodeIp;
    private String tag;
}
