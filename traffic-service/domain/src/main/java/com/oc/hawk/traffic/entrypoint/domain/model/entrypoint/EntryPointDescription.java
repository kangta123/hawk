package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointDescription {

    private String name;
    private String desc;

    public EntryPointDescription(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
