package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@DomainValueObject
@Getter
public class UserID {
    private final long id;

    public UserID(long id) {
        this.id = id;
    }
}
