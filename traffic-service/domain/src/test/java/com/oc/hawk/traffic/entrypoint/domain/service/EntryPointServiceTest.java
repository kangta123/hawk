package com.oc.hawk.traffic.entrypoint.domain.service;

import com.oc.hawk.test.TestHelper;
import com.oc.hawk.traffic.entrypoint.EntryPointBaseTest;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class EntryPointServiceTest extends EntryPointBaseTest {

    @Test
    public void testGetCurrentEntryPointVisibily_groupIdListAlreadyExist() {
        when(entryPointConfigRepository.byGroupIdList(any())).thenReturn(List.of(getEntryPointConfig()));
        
        List<EntryPointConfigGroup> entryPointGroupConfigList = new ArrayList<>();
        entryPointGroupConfigList.add(getEntryPointConfigGroup());

        List<EntryPointConfig> configList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentEntryPointVisibilitiy(entryPointGroupConfigList);
        Assertions.assertThat(configList).isNotEmpty();
    }

    @Test
    public void testGetCurrentEntryPointVisibily_groupIdListNotExist() {
        when(entryPointConfigRepository.byGroupIdList(List.of())).thenReturn(null);

        List<EntryPointConfigGroup> entryPointGroupConfigList = new ArrayList<>();
        List<EntryPointConfig> configList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentEntryPointVisibilitiy(entryPointGroupConfigList);
        Assertions.assertThat(configList).isNull();
    }

    @Test
    public void testGetCurrentGroupList_currentGroupList() {
        when(entryPointConfigRepository.findGroups()).thenReturn(List.of(getEntryPointConfigGroup()));

        List<EntryPointConfigGroup> entryPointConfigGroupList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentGroupList();
        Assertions.assertThat(entryPointConfigGroupList).isNotNull();
    }

    @Test
    public void testGetCurrentAllGroupList_currentAllGroupList() {
        List<EntryPointConfigGroup> visibleGroupList = new ArrayList<>();
        when(entryPointConfigRepository.findAllGroup()).thenReturn(visibleGroupList);

        List<EntryPointConfigGroup> entryPointConfigGroupList = new EntryPointConfigGroups(entryPointConfigRepository).getCurrentAllGroupList();
        Assertions.assertThat(entryPointConfigGroupList).isNotNull();
    }

    /**
     *
     */
    @Test
    public void testUpdateVisibility_updateVisibility() {
        List<EntryPointGroupID> entryPointGroupIdList = new ArrayList<>();
        doNothing().when(entryPointConfigRepository).update(entryPointGroupIdList);

        new EntryPointConfigGroups(entryPointConfigRepository).updateVisibility(entryPointGroupIdList, false);
        verify(entryPointConfigRepository, times(1)).update(entryPointGroupIdList);
    }

    @Test
    public void testGetEntryPointByKey_keyExist() {
        List<EntryPointConfig> entryPointConfigList = new ArrayList<>();
        when(entryPointConfigRepository.byKey(any(), any())).thenReturn(entryPointConfigList);

        List<EntryPointConfig> configList = new EntryPointConfigGroups(entryPointConfigRepository).getEntryPointByKey(str());
        Assertions.assertThat(configList).isNotNull();
    }

    public EntryPointConfig getEntryPointConfig() {
        return TestHelper.newInstance(EntryPointConfig.class);
    }

    public EntryPointConfigGroup getEntryPointConfigGroup() {
        return EntryPointConfigGroup
            .builder()
            .groupId(new EntryPointGroupID(1L))
            .groupName(str())
            .build();
    }
}
