package com.oc.hawk.traffic.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

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
        EntryPointConfigPO apiConfigPO = new EntryPointConfigPO();
        apiConfigPO.setApiName(apiConfig.getDesign().getName());
        apiConfigPO.setApiPath(apiConfig.getHttpResource().getPath().getPath());
        apiConfigPO.setApiMethod(apiConfig.getHttpResource().getMethod().name());
        apiConfigPO.setGroupId(apiConfig.getGroupId().getId());
        apiConfigPO.setApiDesc(apiConfig.getDesign().getDesc());
        apiConfigPO.setApp(apiConfig.getHttpResource().getTarget().getApp());
        apiConfigPO.setProjectId(apiConfig.getHttpResource().getTarget().getProjectId());
        apiConfigPO.setCreateTime(LocalDateTime.now());
        apiConfigPO.setUpdateTime(LocalDateTime.now());
        return apiConfigPO;
    }

    public EntryPointConfig toEntryPointConfig() {
        return EntryPointConfig.builder()
            .configId(new EntryPointConfigID(getId()))
            .groupId(new EntryPointGroupID(groupId))
            .design(new EntryPointDesign(apiName, apiDesc))
            .httpResource(new EntryPointHttpResource(new EntryPointPath(apiPath), EntryPointMethod.valueOf(apiMethod), new EntryPointTarget(app, projectId)))
            .build();
    }

}
