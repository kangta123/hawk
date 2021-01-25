package com.oc.hawk.container.runtime.application.project.representation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.oc.hawk.container.api.command.CreateInstanceVolumeSpecCommand;
import com.oc.hawk.container.api.command.CreateRuntimeInfoSpecCommand;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfig;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeImageConfig;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeVolumeConfig;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeCatalog;
import com.oc.hawk.project.api.dto.ProjectBuildStartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProjectBuildRuntimeRepresentation {
    public static final String PERFORMANCE_UNLIMITED = "UNLIMITED";
    public static final String RUNTIME_CATALOG_BUILD = "build";
    public static final String LABEL_BUILD_JOB_ID = "build_job";
    private static final String LABEL_RUNTIME_CATALOG = "runtime_catalog";
    private static final String ENV_BASE_IMAGE = "BASE_IMAGE";
    private static final String ENV_IMAGE_PREFIX = "IMAGE_PREFIX";
    private final ContainerConfiguration containerConfiguration;

    public CreateRuntimeInfoSpecCommand buildServiceRuntimeSpecDTO(Long buildJobId, ProjectBuildStartDTO data, ProjectRuntimeConfig projectRuntimeConfig) {
        Map<String, Object> env = data.getEnv();
        ProjectRuntimeImageConfig image = projectRuntimeConfig.getImage();
        env.put(ENV_BASE_IMAGE, image.getDataImage());
        env.put(ENV_IMAGE_PREFIX, containerConfiguration.getAppImagePrefix());

        CreateRuntimeInfoSpecCommand spec = new CreateRuntimeInfoSpecCommand();
        spec.setProjectId(data.getProjectId());
        spec.setEnv(Maps.transformValues(env, String::valueOf));
        spec.setRuntimeCatalog(RUNTIME_CATALOG_BUILD);
        spec.setMesh(false);
        spec.setName(getBuildRuntimeName(buildJobId, data));
        spec.setAppImage(image.getBuildImage());
        spec.setNamespace(containerConfiguration.getBuildNamespace());

        spec.setVolume(getVolume(projectRuntimeConfig));
        spec.setLabels(ImmutableMap.of(LABEL_RUNTIME_CATALOG, RuntimeCatalog.BUILD.toString(), LABEL_BUILD_JOB_ID, String.valueOf(buildJobId)));

        return spec;
    }

    private String getBuildRuntimeName(Long buildJobId, ProjectBuildStartDTO data) {
        return "builder-" + buildJobId;
    }

    private List<CreateInstanceVolumeSpecCommand> getVolume(ProjectRuntimeConfig projectRuntimeConfig) {
        ProjectRuntimeVolumeConfig volume = projectRuntimeConfig.getVolume();
        List<CreateInstanceVolumeSpecCommand> volumes = Lists.newArrayList();
        if (volume != null) {
            volumes.add(new CreateInstanceVolumeSpecCommand(volume.getMountPath(), volume.getVolume()));
        }
        return volumes;
    }

}
