package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@Data
@DomainValueObject
public class ProjectBuildJobID {
    public ProjectBuildJobID(Long id) {
        this.id = id;
    }

    private Long id;
}
