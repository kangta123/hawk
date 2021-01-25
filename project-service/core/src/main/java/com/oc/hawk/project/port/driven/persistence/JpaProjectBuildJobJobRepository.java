package com.oc.hawk.project.port.driven.persistence;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oc.hawk.project.domain.model.buildjob.*;
import com.oc.hawk.project.domain.model.project.ProjectID;
import com.oc.hawk.project.port.driven.persistence.po.InstanceImageInfoPO;
import com.oc.hawk.project.port.driven.persistence.po.ProjectBuildJobPO;
import com.oc.hawk.project.port.driven.persistence.po.ProjectBuildStagePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
interface ProjectBuildPORepository extends JpaRepositoryImplementation<ProjectBuildJobPO, Long> {
    @Query(value = "SELECT count(1) FROM project_build_job WHERE date(created_time) = date(now()) AND project_id=:projectId", nativeQuery = true)
    int getProjectBuildTimesByDay(@Param("projectId") long project);

    boolean existsByTagAndProjectId(String customTag, long id);

    List<ProjectBuildJobPO> findByProjectId(long id, Pageable pageable);

    @Query(value = "SELECT max(job)  FROM ProjectBuildJobPO job WHERE job.projectId in (:ids) GROUP BY job.projectId")
    List<ProjectBuildJobPO> queryProjectsLastBuildTime(@Param("ids") List<Long> ids);

    @Query(nativeQuery = true, value = "SELECT " +
        "record.branch as branch, " +
        "job.id as jobId, " +
        "stage.DATA as image, " +
        "job.tag as tag, " +
        "job.created_time as time " +
        "FROM " +
        "project_build_job job " +
        "LEFT JOIN project_build_job_stage stage ON stage.job_id = job.id " +
        "LEFT JOIN project_git_commit_record record ON record.id = job.commit_log_id " +
        "WHERE " +
        "job.project_id = :projectId " +
        "AND stage.DATA IS NOT NULL order by created_time desc limit 30"
    )
    List<InstanceImageInfoPO> queryInstanceImage(long projectId);
}


@Component
@RequiredArgsConstructor
@Slf4j
public class JpaProjectBuildJobJobRepository implements ProjectBuildJobRepository {
    private final ProjectBuildPORepository projectBuildPORepository;
    private final ProjectBuildStagePoRepository projectBuildStagePORepository;

    @Override
    public Map<ProjectID, ProjectBuildJob> findProjectLastBuildJob(List<ProjectID> projectIds) {
        List<Long> ids = projectIds.stream().map(ProjectID::getId).collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Maps.newHashMap();
        }
        return projectBuildPORepository.queryProjectsLastBuildTime(ids).stream()
            .map(po -> po.toProjectBuildJob(null)).collect(Collectors.toMap(ProjectBuildJob::getProjectId, Function.identity()));
    }

    @Override
    public ProjectBuildJob save(ProjectBuildJob projectBuildJob) {
        ProjectBuildJobPO projectBuildJobPo = ProjectBuildJobPO.createdBy(projectBuildJob);
        projectBuildPORepository.save(projectBuildJobPo);

        ProjectBuildExecutionPlan executionPlan = projectBuildJob.getExecutionPlan();
        if (executionPlan != null) {
            List<ProjectBuildStagePO> stages = executionPlan.getStages().stream().map(stage -> ProjectBuildStagePO.createdBy(projectBuildJobPo.getId(), stage)).collect(Collectors.toList());
            projectBuildStagePORepository.saveAll(stages);
        }
        return toProjectBuildJob(projectBuildJobPo);
    }

    @Override
    public Integer findProjectBuildTimesByDay(ProjectID projectId) {
        return projectBuildPORepository.getProjectBuildTimesByDay(projectId.getId());
    }

    @Override
    public Optional<ProjectBuildJob> byId(ProjectBuildJobID projectId) {
        return projectBuildPORepository.findById(projectId.getId()).map(this::toProjectBuildJob);
    }

    @Override
    public boolean isCustomTagExisted(String customTag, ProjectID projectId) {
        return projectBuildPORepository.existsByTagAndProjectId(customTag, projectId.getId());
    }

    @Override
    public List<ProjectBuildJob> queryLatestBuildJobs(long projectId, int size) {
        PageRequest pageRequest = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
        List<ProjectBuildJobPO> projectBuildJobs = projectBuildPORepository.findByProjectId(projectId, pageRequest);
        if (projectBuildJobs == null) {
            return Lists.newArrayList();
        }

        return projectBuildJobs.stream().map(job -> job.toProjectBuildJob(null)).collect(Collectors.toList());
    }

    @Override
    public Set<InstanceImageInfo> queryInstanceImages(long projectId, String tag) {
        List<InstanceImageInfoPO> images = projectBuildPORepository.queryInstanceImage(projectId);
        if (images != null) {
            return images.stream()
                .filter(i -> StringUtils.isEmpty(tag) || StringUtils.equals(i.getTag(), tag))
                .map(i -> new InstanceImageInfo(i.getTag(), i.getImage(), i.getBranch(), i.getJobId(), i.getTime()))
                .collect(Collectors.toSet());
        }
        return null;
    }

    private ProjectBuildJob toProjectBuildJob(ProjectBuildJobPO job) {
        List<ProjectBuildStagePO> projectBuildStages = projectBuildStagePORepository.findByJobId(job.getId());

        return job.toProjectBuildJob(projectBuildStages);
    }
}
