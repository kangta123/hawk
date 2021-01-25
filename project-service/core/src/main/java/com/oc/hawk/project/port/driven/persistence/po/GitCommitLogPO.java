package com.oc.hawk.project.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLog;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLogID;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "project_git_commit_record")
@Entity
public class GitCommitLogPO extends BaseEntity {

    private String branch;
    private String author;
    private String commitName;
    private String commitEmail;
    private String message;
    private String versionNumber;
    private LocalDateTime commitTime;
    private LocalDateTime createTime = LocalDateTime.now();

    public static GitCommitLogPO createBy(GitCommitLog commitLogRecord) {
        GitCommitLogPO gitCommitLogPo = new GitCommitLogPO();

        GitCommitter committer = commitLogRecord.getCommitter();
        gitCommitLogPo.setAuthor(committer.getAuthor());
        gitCommitLogPo.setCommitEmail(committer.getCommitEmail());
        gitCommitLogPo.setCommitName(committer.getCommitName());

        gitCommitLogPo.setMessage(commitLogRecord.getMessage());
        gitCommitLogPo.setVersionNumber(commitLogRecord.getVersionNumber());
        gitCommitLogPo.setBranch(commitLogRecord.getBranch());
        gitCommitLogPo.setCommitTime(commitLogRecord.getCommitTime());

        if (commitLogRecord.getId() != null) {
            gitCommitLogPo.setId(commitLogRecord.getId().getId());
        }
        return gitCommitLogPo;
    }

    public GitCommitLog toProjectBuildGitCommit() {
        return GitCommitLog.builder()
            .committer(new GitCommitter(author, commitName, commitEmail))
            .commitTime(commitTime)
            .branch(branch)
            .id(new GitCommitLogID(this.getId()))
            .message(message)
            .versionNumber(versionNumber)
            .build();
    }
}
