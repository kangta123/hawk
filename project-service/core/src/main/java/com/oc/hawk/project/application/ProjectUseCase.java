package com.oc.hawk.project.application;

import com.google.common.collect.Lists;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.project.api.command.RegisterProjectCommand;
import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import com.oc.hawk.project.api.dto.ProjectListItemDTO;
import com.oc.hawk.project.api.dto.ProjectNameDTO;
import com.oc.hawk.project.api.event.ProjectDomainEventType;
import com.oc.hawk.project.application.representation.ProjectRepresentation;
import com.oc.hawk.project.domain.facade.UserFacade;
import com.oc.hawk.project.domain.model.codebase.CodeBase;
import com.oc.hawk.project.domain.model.codebase.CodeBaseRepository;
import com.oc.hawk.project.domain.model.codebase.git.GitCodeBase;
import com.oc.hawk.project.domain.model.codebase.git.GitRepoKey;
import com.oc.hawk.project.domain.model.codebase.git.GitRepository;
import com.oc.hawk.project.domain.model.project.Project;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.domain.model.project.ProjectName;
import com.oc.hawk.project.domain.model.project.ProjectRepository;
import com.oc.hawk.project.domain.model.user.UserDepartment;
import com.oc.hawk.project.domain.service.ProjectQueryService;
import com.oc.hawk.project.domain.service.ProjectRegisterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectUseCase {

    private final ProjectQueryService projectQueryService;

    private final ProjectFactory projectFactory;
    private final CodeBaseFactory codeBaseFactory;

    private final ProjectRegisterService projectRegisterService;
    private final ProjectRepository projectRepository;

    private final CodeBaseRepository codeBaseRepository;
    private final GitRepository gitRepository;

    private final ProjectRepresentation projectRepresentation;
    private final UserFacade userFacade;
    private final EventPublisher eventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public void registerProject(RegisterProjectCommand command) {
        log.info("Register project with name {}", command.getName());
        CodeBase codeBase = codeBaseFactory.create(command);
        Project project = projectFactory.create(command);

        ProjectID projectId = projectRegisterService.register(project, codeBase);

        ProjectDetailDTO projectDetailDTO = projectRepresentation.toProjectDTO(project, codeBase);
        projectDetailDTO.setId(projectId.getId());

        eventPublisher.publishDomainEvent(DomainEvent.byData(projectId.getId(), ProjectDomainEventType.PROJECT_CREATED, projectDetailDTO));
    }

    @Async
    public void asyncUpdateGitRepo(Long projectId) {
        log.info("Asynchronously update git repo {}", projectId);

        final CodeBase codeBase = codeBaseRepository.byProjectId(new ProjectID(projectId));
        if (codeBase instanceof GitCodeBase) {
            gitRepository.updateCodeRepo(new GitRepoKey(projectId), codeBase);
        } else {
            log.warn("Not support this kind of codebase, {}", projectId);
        }
    }

    @Transactional(readOnly = true)
    public DomainPage<ProjectListItemDTO> queryProjectPage(Integer page,
                                                           Integer size,
                                                           String key) {

        page = page == null ? 0 : page;
        size = size == null ? 10 : size;
        DomainPage<Project> projects = projectQueryService.queryProject(page, size, key);
        return projectRepresentation.toPageDto(projects);
    }

    public ProjectDetailDTO getProject(long id) {
        Project project = projectRepository.byId(new ProjectID(id));

        CodeBase codeBase = codeBaseRepository.byId(project.getCodeBaseId());
        return projectRepresentation.toProjectDTO(project, codeBase);
    }

    public List<ProjectNameDTO> getProjectNames(List<Long> ids) {

        UserDepartment userDepartment = userFacade.currentUserDepartment();
        Map<ProjectID, ProjectName> names = projectRepository.getProjectNames(userDepartment, ids);
        if (names != null) {
            List<ProjectNameDTO> projectNames = Lists.newArrayListWithExpectedSize(names.size());
            names.forEach((k, v) -> projectNames.add(new ProjectNameDTO(k.getId(), v.getName())));
            return projectNames;
        }
        return null;
    }

    public List<Long> getProjectIds() {
        UserDepartment userDepartment = userFacade.currentUserDepartment();
        List<ProjectID> projectIds;
        if (userDepartment.isMaster()) {
            projectIds = projectRepository.getProjectIds(null);
        } else {
            projectIds = projectRepository.getProjectIds(userDepartment.getId());
        }
        return projectIds.stream().map(ProjectID::getId).collect(Collectors.toList());
    }

    public int getProjectTotalCount() {
        return projectRepository.getProjectTotalCount();
    }

    public void deleteProject(long id) {
        log.info("Delete project with id {}", id);
        projectRepository.deleteProject(id);
        eventPublisher.publishDomainEvent(DomainEvent.byType(id, ProjectDomainEventType.PROJECT_DELETED));
    }
}
