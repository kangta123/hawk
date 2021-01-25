package com.oc.hawk.project.domain.model.codebase.git;

import com.google.common.collect.Lists;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

import java.util.List;

@DomainValueObject
@Getter
public class GitProjectBranch {
    private String branch;
    private List<GitCommitLog> commitLogs;


    public GitProjectBranch(String branch) {
        this.branch = branch;
    }

    public void addCommitLog(GitCommitLog commitLog) {
        if (commitLogs == null) {
            commitLogs = Lists.newArrayList();
        }
        commitLogs.add(commitLog);
    }
}

