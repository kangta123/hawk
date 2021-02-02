package com.oc.hawk.project.port.driving.facade.rest;

import com.oc.hawk.project.api.dto.GitCommitRecordDTO;
import com.oc.hawk.project.application.CodebaseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CodeBaseController {
    private final CodebaseUseCase codebaseUseCase;

    @GetMapping("/{projectId}/codebase/branches")
    public List<String> loadBranches(@PathVariable Long projectId) {
        return codebaseUseCase.loadBranches(projectId);
    }

    @GetMapping("/{projectId}/codebase/branches/log/latest")
    public GitCommitRecordDTO getLatestBranchCommitLog(@PathVariable Long projectId, @RequestParam String branch) {
        return codebaseUseCase.getLatestBranchCommitLog(projectId, branch);
    }
}
