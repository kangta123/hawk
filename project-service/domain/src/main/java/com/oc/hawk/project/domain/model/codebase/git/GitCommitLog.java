package com.oc.hawk.project.domain.model.codebase.git;

import com.oc.hawk.ddd.DomainValueObject;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildJobID;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@DomainValueObject
@Getter
public class GitCommitLog {
    private GitCommitLogID id;
    private ProjectBuildJobID jobId;
    private String branch;
    private GitCommitter committer;
    private String message;
    private String versionNumber;
    private LocalDateTime commitTime;
}
