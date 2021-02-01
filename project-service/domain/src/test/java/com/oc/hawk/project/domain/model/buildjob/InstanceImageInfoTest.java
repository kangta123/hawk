package com.oc.hawk.project.domain.model.buildjob;

import com.google.common.collect.Lists;
import com.oc.hawk.project.ProjectBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

class InstanceImageInfoTest extends ProjectBaseTest {
    @Test
    void testSortImageInfoByTime() {
        LocalDateTime now = LocalDateTime.now();
        List<InstanceImageInfo> list = Lists.newArrayList(
            new InstanceImageInfo("tag1", str(), str(), along(), now.minusHours(3)),
            new InstanceImageInfo("tag2", str(), str(), along(), now.minusHours(2)),
            new InstanceImageInfo("tag3", str(), str(), along(), now.minusHours(1)),
            new InstanceImageInfo("tag4", str(), str(), along(), now)
        );

        Collections.sort(list);
        Assertions.assertThat(list).extracting("tag").containsExactly("tag4", "tag3", "tag2", "tag1");

    }
}
