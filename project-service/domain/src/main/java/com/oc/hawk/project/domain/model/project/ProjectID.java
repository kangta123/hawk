package com.oc.hawk.project.domain.model.project;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@Data
@DomainValueObject
public class ProjectID {
    private long id;

    public ProjectID(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
