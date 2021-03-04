package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.ContainerBaseTest;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author kangta123
 */
class InstanceImageTest extends ContainerBaseTest {

    @Test
    void getFullImage_appPrefixIncludeBackslash() {
        final InstanceImage instanceImage = newInstance(InstanceImage.class);

        String appImagePrefix = "appImagePrefix";
        final ContainerConfiguration containerConfiguration = newInstance(ContainerConfiguration.class, "appImagePrefix", appImagePrefix);
        final String fullImage = instanceImage.getFullImage(containerConfiguration);


        Assertions.assertThat(fullImage).startsWith(containerConfiguration.getAppImagePrefix());
    }
}
