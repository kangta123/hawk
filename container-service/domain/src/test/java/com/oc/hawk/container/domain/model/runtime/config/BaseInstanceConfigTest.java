package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.ContainerBaseTest;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.test.TestHelper;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

class BaseInstanceConfigTest extends ContainerBaseTest {
    String tag = "tag";
    String branch = "branch";

    private InstanceImageVersion getVersion(String image) {
        return new InstanceImageVersion(
            Lists.newArrayList(
                new InstanceImage("image1", tag, branch),
                new InstanceImage("image2", tag, branch),
                new InstanceImage(image, tag, branch),
                new InstanceImage("image4", tag, branch)));
    }


    @Test
    void testUpdateNewVersion() {
        final BaseInstanceConfig baseInstanceConfig = TestHelper.newInstance(BaseInstanceConfig.class);
        final InstanceImage image = baseInstanceConfig.getImage();

        final InstanceImageVersion version = getVersion(image.getApp());
        baseInstanceConfig.updateNewVersion(version);

        final InstanceImage image1 = baseInstanceConfig.getImage();
        Assertions.assertThat(image1.getApp()).isEqualTo(image.getApp());
        Assertions.assertThat(image1.getTag()).isEqualTo(tag);
        Assertions.assertThat(image1.getBranch()).isEqualTo(branch);
    }

    @Test
    void testUpdateInstanceVolume_deleteInstanceVolumeIfMountPathIsEmpty() {

        final BaseInstanceConfig baseInstanceConfig = TestHelper.newInstance(BaseInstanceConfig.class);
        final InstanceVolume next = baseInstanceConfig.getVolumes().iterator().next();
        baseInstanceConfig.updateInstanceVolume(next.getVolumeName(), "");
        Assertions.assertThat(baseInstanceConfig.getVolumes()).doesNotContain(next);

    }
}
