package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.oc.hawk.transfer.entrypoint.EntryPointBaseTest;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigID;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;

public class EntryPointConfigTest extends EntryPointBaseTest {
	
	/**
	 * 测试构建接口配置实例(配置id不为空)
	 */
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