package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
@DomainValueObject
@Getter
public class ProjectBuildPostID {
    private Long id;

    public ProjectBuildPostID(Long id) {
        this.id = id;
    }
}
