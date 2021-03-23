package com.oc.hawk.transfer.entrypoint.domain.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.oc.hawk.test.TestHelper;
import com.oc.hawk.transfer.entrypoint.EntryPointBaseTest;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfig;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointConfigGroup;
import com.oc.hawk.transfer.entrypoint.domain.model.entrypointconfig.EntryPointGroupID;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import java.util.ArrayList;
import java.util.List;

public class EntryPointServiceTest extends EntryPointBaseTest{
	
	/**
	 * 测试获取当前可见分组及接口(分组id列表存在)
	 */
	@Test
	public void testGetCurrentEntryPointVisible_groupIdListAlreadyExist() {
		when(entryPointConfigRepository.byGroupIdList(List.of(1L))).thenReturn(List.of(getEntryPointConfig()));
		
		List<EntryPointConfigGroup> entryPointGroupConfigList = new ArrayList<EntryPointConfigGroup>();
		entryPointGroupConfigList.add(getEntryPointConfigGroup());
		
		List<EntryPointConfig> configList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentEntryPointVisibily(entryPointGroupConfigList);
		Assertions.assertThat(configList).isNotEmpty();
	}
	
	/**
	 * 测试获取当前可见分组及接口(分组id列表不存在)
	 */
	@Test
	public void testGetCurrentEntryPointVisible_groupIdListNotExist() {
		when(entryPointConfigRepository.byGroupIdList(List.of())).thenReturn(null);
		
		List<EntryPointConfigGroup> entryPointGroupConfigList = new ArrayList<EntryPointConfigGroup>();
		List<EntryPointConfig> configList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentEntryPointVisibily(entryPointGroupConfigList);
		Assertions.assertThat(configList).isNull();
	}
	
	/**
	 * 测试获取当前可见分组列表(分组存在)
	 */
	@Test
	public void testGetCurrentGroupList_groupListExist() {
		when(entryPointConfigRepository.findGroups()).thenReturn(List.of(getEntryPointConfigGroup()));
		
		List<EntryPointConfigGroup> entryPointConfigGroupList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentGroupList();
		Assertions.assertThat(entryPointConfigGroupList).isNotNull();
	}
	
	/**
	 * 测试获取当前全部分组列表(分组存在)
	 */
	@Test
	public void testGetCurrentAllGroupList_groupListExist() {
		List<EntryPointConfigGroup> visibleGroupList = new ArrayList<>();
		when(entryPointConfigRepository.findAllGroup()).thenReturn(visibleGroupList);
		
		List<EntryPointConfigGroup> entryPointConfigGroupList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentAllGroupList();
		Assertions.assertThat(entryPointConfigGroupList).isNotNull();
	}
	
	/**
	 * 测试设置分组可见度(设置分组id列表存在)
	 */
	@Test
	public void testUpdateVisibility_groupIdListExist() {
		List<EntryPointGroupID> entryPointGroupIdList = new ArrayList<>();
		doNothing().when(entryPointConfigRepository).update(entryPointGroupIdList);
		
		new EntryPointConfigGroups(entryPointConfigRepository).updateVisibility(entryPointGroupIdList, false);
		verify(entryPointConfigRepository, times(1)).update(entryPointGroupIdList);
	}
	
	/**
	 * 测试关键字查询接口(关键字存在)
	 */
	@Test
	public void testGetEntryPointByKey_keyExist() {
		List<EntryPointConfig> entryPointConfigList = new ArrayList<>();
		when(entryPointConfigRepository.byKey(any(), any())).thenReturn(entryPointConfigList);
		
		List<EntryPointConfig> configList = new EntryPointConfigGroups(entryPointConfigRepository).getEntryPointByKey(str());
		Assertions.assertThat(configList).isNotNull();
	}
	
	private EntryPointConfig getEntryPointConfig() {
        return TestHelper.newInstance(EntryPointConfig.class);
    }
	
	private EntryPointConfigGroup getEntryPointConfigGroup() {
		return EntryPointConfigGroup
				.builder()
				.groupId(new EntryPointGroupID(1L))
				.groupName(str())
				.build();
	}
}
