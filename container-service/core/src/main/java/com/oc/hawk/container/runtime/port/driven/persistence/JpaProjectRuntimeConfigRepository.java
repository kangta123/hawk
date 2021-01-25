package com.oc.hawk.container.runtime.port.driven.persistence;

import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfigRepository;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfig;
import com.oc.hawk.container.runtime.port.driven.persistence.po.ProjectRuntimeConfigPO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
interface ProjectBuildConfigPoRepository extends CrudRepository<ProjectRuntimeConfigPO, Long> {
    ProjectRuntimeConfigPO findByBuildTypeAndRuntimeType(String buildType, String runtimeType);

    List<ProjectRuntimeConfigPO> findByRuntimeType(String runtimeType);
}

@Component
@RequiredArgsConstructor
public class JpaProjectRuntimeConfigRepository implements ProjectRuntimeConfigRepository {

    private final ProjectBuildConfigPoRepository projectBuildConfigPORepository;

    @Override
    public ProjectRuntimeConfig findBy(String buildType, String runtimeType) {
        return projectBuildConfigPORepository.findByBuildTypeAndRuntimeType(buildType, runtimeType).toConfig();
    }

    @Override
    public List<ProjectRuntimeConfig> findBy(String runtimeType) {
        List<ProjectRuntimeConfigPO> configList = projectBuildConfigPORepository.findByRuntimeType(runtimeType);


        return configList.stream().map(ProjectRuntimeConfigPO::toConfig).collect(Collectors.toList());
    }
}
