package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.ddd.DomainEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@DomainEntity
public class EntryPointConfig {

    /**
     * 配置id
     */
    private final EntryPointConfigID configId;
    /**
     * 配置：名称、配置描述
     */
    private final EntryPointDescription description;
    /**
     * 配置分组
     */
    private final EntryPointGroupID groupId;
    /**
     * 配置资源
     */
    private final HttpResource httpResource;
    /**
     * 项目Id
     */
    private Long projectId;
    
    public void updateProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
}
