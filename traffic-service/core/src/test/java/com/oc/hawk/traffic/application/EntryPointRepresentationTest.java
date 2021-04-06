package com.oc.hawk.traffic.application;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oc.hawk.traffic.application.entrypoint.representation.EntryPointConfigRepresentation;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ContainerFacade;
import com.oc.hawk.traffic.application.entrypoint.representation.facade.ProjectFacade;
import com.oc.hawk.traffic.entrypoint.api.dto.UserGroupEntryPointDTO;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigID;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointDesign;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointHttpResource;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointPath;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointTarget;

public class EntryPointRepresentationTest extends TrafficBaseTest {
    
    EntryPointConfigRepresentation entryPointConfigRepresentation;
    ContainerFacade containerFacade;
    ProjectFacade projectFacade;
    
    @BeforeEach
    public void setup() {
        entryPointConfigRepresentation = new EntryPointConfigRepresentation(containerFacade,projectFacade);
    }
    
    /**
     * 测试转换用户分组及api列表_分组已存在
     */
    @Test
    public void testToUserGroupEntryPointDTOList_groupAlreadyExists() {
        List<EntryPointConfig> entryPointList = List.of(getEntryPointConfig(),getEntryPointConfig(),getEntryPointConfig());
        List<EntryPointConfigGroup> entryPointGroupList = List.of(getEntryPointConfigGroup());
        
        List<UserGroupEntryPointDTO> dtoList = entryPointConfigRepresentation.toUserGroupEntryPointDTOList(entryPointList,entryPointGroupList);
        Assertions.assertThat(dtoList.get(0)).isNotNull();
    }
    
    EntryPointConfig getEntryPointConfig() {
        EntryPointDesign design = new EntryPointDesign(str(),str());
        EntryPointHttpResource resource = new EntryPointHttpResource(new EntryPointPath(str()),EntryPointMethod.GET,new EntryPointTarget(str(),along()));
        return EntryPointConfig.builder()
                .configId(new EntryPointConfigID(along()))
                .groupId(new EntryPointGroupID(1L))
                .design(design)
                .httpResource(resource)
                .build();
    }
    
    EntryPointConfigGroup getEntryPointConfigGroup() {
        return EntryPointConfigGroup.builder()
                .groupId(new EntryPointGroupID(1L))
                .groupName(str())
                .build();
    }
    
}
