package com.oc.hawk.project.domain.model.projectapp;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class ProjectAppID {
    private final Long id;

    public ProjectAppID(Long id) {
        this.id = id;
    }
}
