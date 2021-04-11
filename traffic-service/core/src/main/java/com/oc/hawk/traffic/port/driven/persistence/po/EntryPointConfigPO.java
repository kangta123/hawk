package com.oc.hawk.traffic.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "traffic_entrypoint")
@Entity
@DynamicUpdate
public class EntryPointConfigPO extends BaseEntity {

    private Long groupId;
    private String apiName;
    private String apiPath;
    private String apiMethod;
    private String apiDesc;
    private Long projectId;
    private String app;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static EntryPointConfigPO createBy(EntryPointConfig apiConfig) {
        EntryPointConfigPO apiConfigPo = new EntryPointConfigPO();
        apiConfigPo.setApiName(apiConfig.getDescription().getName());
        apiConfigPo.setApiPath(apiConfig.getHttpResource().getPath().getPath());
        apiConfigPo.setApiMethod(apiConfig.getHttpResource().getMethod().name());
        apiConfigPo.setGroupId(apiConfig.getGroupId().getId());
        apiConfigPo.setApiDesc(apiConfig.getDescription().getDesc());
        //apiConfigPo.setApp(apiConfig.getHttpResource().getTarget().getApp());
        apiConfigPo.setProjectId(apiConfig.getProjectId());
        apiConfigPo.setCreateTime(LocalDateTime.now());
        apiConfigPo.setUpdateTime(LocalDateTime.now());
        return apiConfigPo;
    }

    public EntryPointConfig toEntryPointConfig() {
        return EntryPointConfig.builder()
            .configId(new EntryPointConfigID(getId()))
            .groupId(new EntryPointGroupID(groupId))
            .description(new EntryPointDescription(apiName, apiDesc))
            .httpResource(new HttpResource(new HttpPath(apiPath), HttpMethod.valueOf(apiMethod)))
            .projectId(projectId)
            .build();
    }

}
