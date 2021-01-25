package com.oc.hawk.project.domain.model.project;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@DomainValueObject
@Data
public class ProjectName {
    private String name;

    public ProjectName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
