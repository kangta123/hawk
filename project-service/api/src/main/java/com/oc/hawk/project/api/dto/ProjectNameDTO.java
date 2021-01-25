package com.oc.hawk.project.api.dto;

import lombok.Data;

@Data
public class ProjectNameDTO {
    private Long id;
    private String name;

    public ProjectNameDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
