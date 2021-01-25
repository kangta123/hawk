package com.oc.hawk.project.application;

import com.oc.hawk.project.api.dto.GitCommitRecordDTO;
import com.oc.hawk.project.application.representation.CodeBaseRepresentation;
import com.oc.hawk.project.domain.model.codebase.CodeBaseRepository;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLog;
import com.oc.hawk.project.domain.model.codebase.git.GitRepoKey;
import com.oc.hawk.project.domain.model.codebase.git.GitRepository;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.service.GitCodebaseFetchLatestCommitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodebaseUseCase {
    private final GitRepository gitRepository;
    private final CodeBaseRepository codeBaseRepository;
    private final CodeBaseRepresentation codeBaseRepresentation;
    private final GitCodebaseFetchLatestCommitLogService gitCodebaseFetchLatestCommitLogService;

    public List<String> loadBranches(Long projectId) {
        GitCodeBase codeBase = (GitCodeBase) codeBaseRepository.byProjectId(new ProjectID(projectId));
        final GitRepoKey key = new GitRepoKey(projectId);
        gitRepository.updateCodeRepo(key, codeBase);
        return codeBase.loadGitBranches(key, gitRepository);
    }

    public GitCommitRecordDTO getLatestBranchCommitLog(Long projectId, String branch) {
        GitCommitLog gitCommitLog = gitCodebaseFetchLatestCommitLogService.getGitCommitLog(new ProjectID(projectId), branch);
        return codeBaseRepresentation.toGitCommitRecordDTO(gitCommitLog);
    }

}

