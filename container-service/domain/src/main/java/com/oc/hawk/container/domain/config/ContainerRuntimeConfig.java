package com.oc.hawk.container.domain.config;

import com.oc.hawk.container.domain.model.runtime.config.volume.BuildInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.NormalInstanceVolume;
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

        return new ContainerConfig(runtimeConfig, buildConfig);
    }


    public static class ContainerConfig {
        private final RuntimeConfig runtime;
        private final BuildConfig build;

        public ContainerConfig(RuntimeConfig runtime, BuildConfig build) {
            this.runtime = runtime;
            this.build = build;
        }

        public String getDataImage() {
            return runtime.getDataImage();
        }

        public String getBuildImage() {
            return build.getImage();
        }

        public List<InstanceVolume> getBuildVolumes() {
            final List<InstanceVolume> buildInstanceVolumes = BuildInstanceVolume.defaultDockerVolumes();
            VolumeConfig volumeConfig = build.getVolume();
            if (volumeConfig != null) {
                buildInstanceVolumes.add(new NormalInstanceVolume(volumeConfig.getVolume(), volumeConfig.getMountPath()));
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
        private String volume;
    }
}
