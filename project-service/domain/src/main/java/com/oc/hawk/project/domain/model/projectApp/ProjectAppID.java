package com.oc.hawk.project.domain.model.projectApp;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class ProjectAppID {
    private Long id;

    public ProjectAppID(Long id) {
        this.id = id;
    }
}
