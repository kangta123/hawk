package com.oc.hawk.container.domain.config;

import com.oc.hawk.container.domain.ContainerBaseTest;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kangta123
 */
class ContainerConfigTest extends ContainerBaseTest {

    @Test
    public void testGetBuildVolumes_mountPathCannotBeNull() {
        final ContainerRuntimeConfig.ContainerConfig containerConfig = newContainerConfig();
        final List<InstanceVolume> volumes = containerConfig.getBuildVolumes();

        for (InstanceVolume v : volumes) {
            Assertions.assertThat(v.getMountPath()).isNotEmpty();
        }
    }

    protected ContainerRuntimeConfig.ContainerConfig newContainerConfig() {
        return new ContainerRuntimeConfig.ContainerConfig(newInstance(ContainerRuntimeConfig.RuntimeConfig.class), newInstance(ContainerRuntimeConfig.BuildConfig.class), str());
    }

}
