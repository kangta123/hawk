package com.oc.hawk.project.port.driven.persistence;

import com.oc.hawk.project.domain.model.buildjob.ProjectBuildPost;
import com.oc.hawk.project.domain.model.buildjob.ProjectBuildPostRepository;
import com.oc.hawk.project.port.driven.persistence.po.ProjectBuildPostPO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
interface ProjectBuildPostPoRepository extends JpaRepositoryImplementation<ProjectBuildPostPO, Long> {
    ProjectBuildPostPO findByProjectBuildId(long jobId);
}

@Component
@RequiredArgsConstructor
public class JpaProjectBuildPostRepository implements ProjectBuildPostRepository {
    private final ProjectBuildPostPoRepository projectBuildPostPORepository;

    @Override
    public ProjectBuildPost byBuildJobId(long jobId) {
        ProjectBuildPostPO buildPostPo = projectBuildPostPORepository.findByProjectBuildId(jobId);
        if (buildPostPo != null) {
            return buildPostPo.toBuildPost();
        }
        return null;
    }

    @Override
    public void save(ProjectBuildPost projectBuildPost) {
        if (projectBuildPost != null && projectBuildPost.getDeployTo() != null) {
            final ProjectBuildPostPO projectBuildPostPo = ProjectBuildPostPO.create(projectBuildPost);
            projectBuildPostPORepository.save(projectBuildPostPo);
        }
    }
}
