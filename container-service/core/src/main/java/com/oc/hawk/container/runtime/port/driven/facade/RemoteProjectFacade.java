package com.oc.hawk.container.runtime.port.driven.facade;

import com.google.common.collect.Lists;
import com.oc.hawk.common.spring.mvc.BooleanWrapper;
import com.oc.hawk.container.domain.facade.ProjectFacade;
import com.oc.hawk.container.domain.model.runtime.build.ProjectBuild;
import com.oc.hawk.container.domain.model.runtime.build.ProjectBuildPost;
import com.oc.hawk.container.domain.model.runtime.build.ProjectType;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import com.oc.hawk.container.domain.model.runtime.config.InstanceImage;
import com.oc.hawk.container.domain.model.runtime.config.InstanceImageVersion;
import com.oc.hawk.container.runtime.port.driven.facade.feign.ProjectGateway;
import com.oc.hawk.project.api.dto.InstanceImageDTO;
import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import com.oc.hawk.project.api.dto.ProjectDetailDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class RemoteProjectFacade implements ProjectFacade {
    private final ProjectGateway projectGateway;

    @Override
    public Integer getProjectTotalCount() {
        BooleanWrapper booleanWrapper = projectGateway.queryProjectCount();
        return Integer.valueOf(String.valueOf(booleanWrapper.getMessage()));
    }

    @Override
    public List<Long> getProjectIds() {
        return projectGateway.queryVisibleProjectIds();
    }

    @Override
    public ProjectType getProjectType(Long id) {
        ProjectDetailDTO projectDTO = projectGateway.getProject(id);
        return new ProjectType(projectDTO.getProjectType(), projectDTO.getBuildType());
    }


    @Override
    public InstanceImageVersion getProjectBuildImage(long projectId, String tag) {
        List<InstanceImageDTO> instanceImages = projectGateway.getInstanceImages(projectId, tag);
        if (!CollectionUtils.isEmpty(instanceImages)) {
            final List<InstanceImage> images = instanceImages.stream().map(i -> new InstanceImage(i.getApp(), i.getTag(), i.getBranch())).collect(Collectors.toList());
            return new InstanceImageVersion(images);
        }
        return null;
    }

    @Override
    public ProjectBuild getProjectBuild(long buildJobId) {
        final ProjectBuildJobDTO buildJob = projectGateway.getProjectBuildJob(buildJobId);
        final List<String> apps = buildJob.getApps();
        final List<InstanceImage> instanceImages;
        if (apps != null) {
            instanceImages = apps.stream().map(m -> new InstanceImage(m, buildJob.getTag(), buildJob.getBranch())).collect(Collectors.toList());
        } else {
            instanceImages = Lists.newArrayList();
        }
        return new ProjectBuild(getProjectBuildPost(buildJob), new InstanceImageVersion(instanceImages));
    }

    private ProjectBuildPost getProjectBuildPost(ProjectBuildJobDTO buildJob) {
        if (buildJob != null && buildJob.getDeployToId() != null) {
            return new ProjectBuildPost(new InstanceId(buildJob.getDeployToId()), buildJob.getDeployToInstance());
        }
        return null;
    }

}
