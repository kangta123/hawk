package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class ProjectBuildStageID {
    private final Long id;

    public ProjectBuildStageID(Long id) {
        this.id = id;
    }
}
