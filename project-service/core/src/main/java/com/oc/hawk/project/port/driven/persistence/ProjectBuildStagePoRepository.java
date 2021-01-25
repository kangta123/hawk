package com.oc.hawk.project.port.driven.persistence;

import com.oc.hawk.project.port.driven.persistence.po.ProjectBuildStagePO;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectBuildStagePoRepository extends JpaRepositoryImplementation<ProjectBuildStagePO, Long> {
    List<ProjectBuildStagePO> findByJobId(Long id);
}
