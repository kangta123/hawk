package com.oc.hawk.container.domain.model.runtime;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@Getter
@DomainValueObject
public class UserInfo {
    private String name;
    private Long id;

    public UserInfo(String name, Long id) {
        this.name = name;
        this.id = id;
    }
}
