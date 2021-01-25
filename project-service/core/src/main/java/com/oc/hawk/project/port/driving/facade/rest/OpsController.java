package com.oc.hawk.project.port.driving.facade.rest;

import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.CodeBaseID;
import com.oc.hawk.project.domain.model.codebase.git.GitRepoKey;
import com.oc.hawk.project.port.driven.persistence.GitCodeBaseRepository;
import com.oc.hawk.project.port.driven.persistence.GitRepo;
import com.oc.hawk.project.port.driven.persistence.JpaProjectRepository;
import com.oc.hawk.project.port.driven.persistence.RemoteGitRepository;
import com.oc.hawk.project.port.driven.persistence.po.ProjectPO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/ops")
@RequiredArgsConstructor
@Slf4j
public class OpsController {
    private final RemoteGitRepository remoteGitRepository;
    private final JpaProjectRepository jpaProjectRepository;
    private final GitCodeBaseRepository gitCodeBaseRepository;

    @GetMapping("/clone")
    public BooleanWrapper cloneRepo(@RequestParam(required = false) Integer size) {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<ProjectPO> allProjects = jpaProjectRepository.getAllProjects();
        if (size != null) {
            allProjects = allProjects.subList(0, size);
        }
        allProjects.forEach(p -> {
            executorService.submit(() -> {
                final CodeBase codeBase = gitCodeBaseRepository.byId(new CodeBaseID(p.getCodeBaseId()));
                final GitRepo gitRepo = remoteGitRepository.getGitRepo(new GitRepoKey(p.getId()), codeBase);
                gitRepo.sync();
                log.info("Clone git from project {}", p.getName());
            });
        });
        return BooleanWrapper.TRUE;
    }
}
