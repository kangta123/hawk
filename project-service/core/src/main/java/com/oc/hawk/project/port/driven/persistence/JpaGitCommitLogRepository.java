package com.oc.hawk.project.port.driven.persistence;

import com.oc.hawk.project.domain.model.codebase.git.GitCommitLog;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLogID;
import com.oc.hawk.project.domain.model.gitrecord.GitCommitLogRepository;
import com.oc.hawk.project.port.driven.persistence.po.GitCommitLogPO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
interface GitCommitLogPORepository extends JpaRepositoryImplementation<GitCommitLogPO, Long> {

}

@Component
@RequiredArgsConstructor
public class JpaGitCommitLogRepository implements GitCommitLogRepository {
    private final GitCommitLogPORepository gitCommitLogPORepository;

    @Override
    public GitCommitLogID save(GitCommitLog log) {
        if (log == null) {
            return null;
        }
        final GitCommitLogPO commitLogPo = GitCommitLogPO.createBy(log);
        gitCommitLogPORepository.save(commitLogPo);
        return new GitCommitLogID(commitLogPo.getId());
    }

    @Override
    public GitCommitLog byId(Long id) {
        return gitCommitLogPORepository.findById(id).map(GitCommitLogPO::toProjectBuildGitCommit).orElse(null);
    }

}
