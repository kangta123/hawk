package com.oc.hawk.project.application;

import com.oc.hawk.project.domain.model.codebase.git.GitCommitLog;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLogID;
import com.oc.hawk.project.domain.model.gitrecord.GitCommitLogRepository;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.service.GitCodebaseFetchLatestCommitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GitCommitLogUseCase {
    private final GitCommitLogRepository gitCommitLogRepository;
    private final GitCodebaseFetchLatestCommitLogService gitCodebaseFetchLatestCommitLogService;

    public GitCommitLogID getLatestGitCommitLog(ProjectID projectId, String branch) {
        GitCommitLog gitCommitLog = gitCodebaseFetchLatestCommitLogService.getGitCommitLog(projectId, branch);
        return gitCommitLogRepository.save(gitCommitLog);
    }
}
