package com.oc.hawk.project.domain.model.project;

import com.oc.hawk.ddd.web.DomainPage;
import com.oc.hawk.project.domain.model.user.UserDepartment;

import java.util.List;
import java.util.Map;

public interface ProjectRepository {
    boolean existsProjectName(ProjectName name);

    ProjectID save(Project project);

    Project byId(ProjectID id);

    DomainPage<Project> projectPage(int page, int size, String key, Long departmentId);

    List<Project> byIds(List<Long> projectIds);

    Map<ProjectID, ProjectName> getProjectNames(UserDepartment userDepartment, List<Long> ids);

    List<ProjectID> getProjectIds(Long id);

    int getProjectTotalCount();

    void deleteProject(long id);
}
