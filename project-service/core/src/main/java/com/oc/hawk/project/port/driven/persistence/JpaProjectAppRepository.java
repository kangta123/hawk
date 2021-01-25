package com.oc.hawk.project.port.driven.persistence;

import com.oc.hawk.project.domain.model.projectApp.ProjectApp;
import com.oc.hawk.project.domain.model.projectApp.ProjectAppID;
import com.oc.hawk.project.domain.model.project.ProjectAppRepository;
import com.oc.hawk.project.port.driven.persistence.po.ProjectAppPO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
interface ProjectAppPORepository extends JpaRepositoryImplementation<ProjectAppPO, Long> {
    List<ProjectAppPO> findByProjectId(long id);

    List<ProjectAppPO> findByIdIn(List<Long> collect);
}

@Component
@RequiredArgsConstructor
public class JpaProjectAppRepository implements ProjectAppRepository {
    private final ProjectAppPORepository projectAppPORepository;

    @Override
    public List<ProjectApp> byProjectId(long id) {
        List<ProjectAppPO> projectAppPos = projectAppPORepository.findByProjectId(id);
        if (projectAppPos != null) {
            return projectAppPos.stream().map(ProjectAppPO::toProjectApp).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<ProjectApp> byIds(List<ProjectAppID> appIds) {
        List<ProjectAppPO> apps = projectAppPORepository.findByIdIn(appIds.stream().map(ProjectAppID::getId).collect(Collectors.toList()));
        if(apps != null){
            return apps.stream().map(ProjectAppPO::toProjectApp).collect(Collectors.toList());
        }
        return null;
    }
}
