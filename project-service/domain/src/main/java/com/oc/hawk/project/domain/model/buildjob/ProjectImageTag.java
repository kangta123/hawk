package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class ProjectImageTag {
    private String tag;

    public ProjectImageTag(String tag) {
        this.tag = tag;
    }
}
