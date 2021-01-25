package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@DomainValueObject
@Data
public class GitCommitLogID {
    private long id;

    public GitCommitLogID(long id) {
        this.id = id;
    }
}
