package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.oc.hawk.transfer.entrypoint.EntryPointBaseTest;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;

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