package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.traffic.entrypoint.EntryPointBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntryPointConfigGroupTest extends EntryPointBaseTest {

    @Test
    public void testEntryPointConfigGroup() {
        final EntryPointConfigGroup group = EntryPointConfigGroup.builder()
            .groupId(new EntryPointGroupID(along()))
            .groupName(str())
            .build();
        Assertions.assertThat(group.getGroupId()).isNotNull();
        Assertions.assertThat(group.getGroupName()).isNotNull();
    }
}
