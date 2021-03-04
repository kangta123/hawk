package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@DomainValueObject
@Data
public class CodeBaseIdentity {
    private String username;
    private String key;

    public CodeBaseIdentity(String username, String key) {
        this.username = username;
        this.key = key;
    }
}
