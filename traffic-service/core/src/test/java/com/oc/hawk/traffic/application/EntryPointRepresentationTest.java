package com.oc.hawk.traffic.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oc.hawk.project.api.dto.ProjectDetailDTO;
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
import com.oc.hawk.traffic.port.driven.facade.RemoteProjectFacade;
import com.oc.hawk.traffic.port.driven.facade.feign.ProjectGateway;

public class EntryPointRepresentationTest extends TrafficBaseTest {
    
    EntryPointConfigRepresentation entryPointConfigRepresentation;
    ProjectFacade projectFacade;
    ProjectGateway projectGateway;
    
    @BeforeEach
    public void setup() {
        projectFacade = new RemoteProjectFacade(projectGateway);
        entryPointConfigRepresentation = new EntryPointConfigRepresentation(projectFacade);
    }
    
    /**
     * 测试转换用户分组及api列表_分组已存在
     */
    @Test
    public void testToUserGroupEntryPointDTOList_groupAlreadyExists() {
        List<EntryPointConfig> entryPointList = List.of(getEntryPointConfig(),getEntryPointConfig(),getEntryPointConfig());
        
        UserGroupEntryPointDTO result = entryPointConfigRepresentation.toUserGroupEntryPointDTO(getEntryPointConfigGroup(),entryPointList);
        Assertions.assertThat(result).isNotNull();
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
