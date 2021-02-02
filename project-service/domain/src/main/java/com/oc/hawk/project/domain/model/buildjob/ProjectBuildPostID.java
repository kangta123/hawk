package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class ProjectBuildPostID {
    private final Long id;

    public ProjectBuildPostID(Long id) {
        this.id = id;
    }
}
