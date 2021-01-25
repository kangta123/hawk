package com.oc.hawk.project.application.representation;

import com.oc.hawk.project.api.dto.GitCommitRecordDTO;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitLog;
import com.oc.hawk.project.domain.model.codebase.git.GitCommitter;
import org.springframework.stereotype.Component;

@Component
public class CodeBaseRepresentation {
    public GitCommitRecordDTO toGitCommitRecordDTO(GitCommitLog commitLogRecord) {
        if (commitLogRecord == null) {
            return null;
        }
        GitCommitRecordDTO record = new GitCommitRecordDTO();

        GitCommitter committer = commitLogRecord.getCommitter();
        record.setAuthor(committer.getAuthor());
        record.setCommitEmail(committer.getCommitEmail());
        record.setCommitName(committer.getCommitName());
        record.setCommitTime(commitLogRecord.getCommitTime());
        record.setMessage(commitLogRecord.getMessage());
        record.setVersionNumber(commitLogRecord.getVersionNumber());

        return record;
    }

}
