package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@DomainValueObject
@Data

public class JobCreator {
    private UserID createId;
    private String name;

    public JobCreator(UserID createId, String name) {
        this.createId = createId;
        this.name = name;
    }
}
