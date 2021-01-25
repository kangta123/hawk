package com.oc.hawk.project.domain.model.codebase;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@Data
@DomainValueObject
public class CodeBaseID {
    private long id;

    public CodeBaseID(Long id) {
        this.id = id;
    }
}
