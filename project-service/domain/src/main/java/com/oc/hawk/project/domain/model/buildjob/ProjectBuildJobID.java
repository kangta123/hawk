package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@Data
@DomainValueObject
public class ProjectBuildJobID {
    private Long id;

    public ProjectBuildJobID(Long id) {
        this.id = id;
    }
}
