package com.oc.hawk.container.domain.config;

import com.oc.hawk.container.domain.model.runtime.build.ProjectBuildVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.NormalInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.SharedInstanceVolume;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author kangta123
 */
@Setter
public class ContainerRuntimeConfig {
    private Map<String, Config> config;
    private String buildVolume;

    public ContainerConfig getConfig(String runtime, String build) {
        final Config containerConfig = config.get(runtime.toLowerCase());
        if (config == null) {
            throw new RuntimeException("Invalid runtime config name " + runtime);
        }
        final RuntimeConfig runtimeConfig = containerConfig.getRuntime();
        if (runtimeConfig == null) {
            throw new RuntimeException("Invalid runtime config  " + runtime);
        }
        final BuildConfig buildConfig = containerConfig.getBuild().get(build.toLowerCase());
        if (buildConfig == null) {
            throw new RuntimeException("Invalid build config name " + build);
        }

        return new ContainerConfig(runtimeConfig, buildConfig, buildVolume);
    }


    public static class ContainerConfig {
        private final RuntimeConfig runtime;
        private final BuildConfig build;
        private String buildVolume;

        public ContainerConfig(RuntimeConfig runtime, BuildConfig build, String buildVolume) {
            this.runtime = runtime;
            this.build = build;
            this.buildVolume = buildVolume;
        }

        public String getDataImage() {
            return runtime.getDataImage();
        }

        public String getBuildImage() {
            return build.getImage();
        }

        public List<InstanceVolume> getBuildVolumes() {
            final List<InstanceVolume> buildInstanceVolumes = ProjectBuildVolume.defaultVolumes();
            VolumeConfig volumeConfig = build.getVolume();
            if (volumeConfig != null) {
                buildInstanceVolumes.add(new SharedInstanceVolume(buildVolume, volumeConfig.getMountPath(), volumeConfig.getSub()));
            }
            return buildInstanceVolumes;
        }

        public String getAppImage() {
            return runtime.getAppImage();
        }
    }

    @Data
    static class Config {
        private RuntimeConfig runtime;
        private Map<String, BuildConfig> build;
    }

    @Data
    static class RuntimeConfig {
        private String appImage;
        private String dataImage;
        private String podInteractionVolume;
    }

    @Data
    static class BuildConfig {
        private String image;
        private VolumeConfig volume;
    }

    @Data
    static class VolumeConfig {
        private String mountPath;
        private String sub;
    }
}
