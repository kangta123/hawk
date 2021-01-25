package com.oc.hawk.project.domain.model.gitrecord;

import com.oc.hawk.project.domain.model.codebase.git.GitCommitLog;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLogID;

public interface GitCommitLogRepository {
    GitCommitLogID save(GitCommitLog log);

    GitCommitLog byId(Long id);

}
