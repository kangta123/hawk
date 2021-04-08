package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.ddd.DomainEntity;
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
    private final EntryPointDesign design;
    /**
     * 配置分组
     */
    private final EntryPointGroupID groupId;
    /**
     * 配置资源
     */
    private final EntryPointHttpResource httpResource;
    
}
