package com.oc.hawk.container.api.dto;

import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ServiceAppDTO {
    private String app;
    private Integer scale;
    private ProjectDetailDTO project;
    private String name;
    private String descn;
    private Long id;
    private String domain;
    private List<ServiceAppVersionDTO> versions;

    public ServiceAppDTO(String app) {
        this.app = app;
    }
}
