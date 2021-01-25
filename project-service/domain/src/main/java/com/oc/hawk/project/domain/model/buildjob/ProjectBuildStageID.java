package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class ProjectBuildStageID {
    public ProjectBuildStageID(Long id) {
        this.id = id;
    }

    private Long id;
}
