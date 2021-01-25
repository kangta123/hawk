package com.oc.hawk.container.domain.model.runtime.spac;

import com.oc.hawk.container.domain.ContainerBaseTest;
import com.oc.hawk.container.domain.model.runtime.info.RuntimeImage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RuntimeImageTest extends ContainerBaseTest {
    @Test
    void testGetTag() {
        String tag = new RuntimeImage("hawk.image.test:v1").getTag();
        Assertions.assertThat(tag).isEqualTo("v1");
    }
}
