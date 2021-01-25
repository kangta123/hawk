package com.oc.hawk.project.domain.service;

import com.oc.hawk.project.domain.model.buildjob.exception.LoadGitCommitLogException;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.CodeBaseRepository;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLog;
import com.oc.hawk.project.domain.model.codebase.git.GitRepoKey;
import com.oc.hawk.project.domain.model.codebase.git.GitRepository;
import com.oc.hawk.project.domain.model.project.ProjectID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GitCodebaseFetchLatestCommitLogService {
    private final CodeBaseRepository codeBaseRepository;
    private final GitRepository gitRepository;

    public GitCommitLog getGitCommitLog(ProjectID projectId, String branch) {
        CodeBase codeBase = codeBaseRepository.byProjectId(projectId);

        if (codeBase instanceof GitCodeBase) {
            return ((GitCodeBase) codeBase).getLatestCommitLog(new GitRepoKey(projectId), branch, gitRepository);
        }
        throw new LoadGitCommitLogException("无法获取Git提交记录");

    }
}
