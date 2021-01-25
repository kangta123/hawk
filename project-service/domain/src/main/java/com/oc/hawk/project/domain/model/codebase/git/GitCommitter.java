package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@DomainValueObject
@Data
@AllArgsConstructor
public class GitCommitter {
    private String author;
    private String commitName;
    private String commitEmail;
}
