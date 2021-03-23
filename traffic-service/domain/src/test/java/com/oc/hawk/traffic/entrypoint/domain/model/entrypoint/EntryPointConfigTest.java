package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.traffic.entrypoint.EntryPointBaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class EntryPointConfigTest extends EntryPointBaseTest {

    @Test
    public void testEntryPointConfig_configIdIsNotNull() {
        final EntryPointConfig entryPointConfig =
            EntryPointConfig.builder()
                .configId(new EntryPointConfigID(along()))
                .groupId(new EntryPointGroupID(along()))
                .build();
        Assertions.assertThat(entryPointConfig.getConfigId()).isNotNull();
    }


}
