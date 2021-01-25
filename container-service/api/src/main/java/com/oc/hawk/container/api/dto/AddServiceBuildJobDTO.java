package com.oc.hawk.container.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddServiceBuildJobDTO {
    private String branch;
    private Long projectId;
    private Long creator;
    private String creatorName;
    private String version;

    private List<Long> appIds;
}
