package com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.oc.hawk.transfer.entrypoint.EntryPointBaseTest;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;

public class EntryPointConfigGroupTest extends EntryPointBaseTest {

	/**
	 * 测试构建配置分组实例(分组id不为空)
	 */
	@Test
	public void testEntryPointConfigGroup_groupIdIsNotNull() {
		final EntryPointConfigGroup group = EntryPointConfigGroup.builder()
				.groupId(new EntryPointGroupID(along()))
				.groupName(str())
				.build();
		Assertions.assertThat(group.getGroupId()).isNotNull();
		Assertions.assertThat(group.getGroupName()).isNotNull();
	}
}
