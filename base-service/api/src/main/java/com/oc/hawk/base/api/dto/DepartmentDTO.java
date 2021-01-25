package com.oc.hawk.base.api.dto;

import lombok.Data;

@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private String projectName;
    private Boolean master;
}
