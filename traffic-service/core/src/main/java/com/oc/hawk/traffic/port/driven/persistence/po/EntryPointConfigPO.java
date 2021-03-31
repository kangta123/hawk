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
        EntryPointConfigPO apiConfigPo = new EntryPointConfigPO();
        apiConfigPo.setApiName(apiConfig.getDesign().getName());
        apiConfigPo.setApiPath(apiConfig.getHttpResource().getPath().getPath());
        apiConfigPo.setApiMethod(apiConfig.getHttpResource().getMethod().name());
        apiConfigPo.setGroupId(apiConfig.getGroupId().getId());
        apiConfigPo.setApiDesc(apiConfig.getDesign().getDesc());
        apiConfigPo.setApp(apiConfig.getHttpResource().getTarget().getApp());
        apiConfigPo.setProjectId(apiConfig.getHttpResource().getTarget().getProjectId());
        apiConfigPo.setCreateTime(LocalDateTime.now());
        apiConfigPo.setUpdateTime(LocalDateTime.now());
        return apiConfigPo;
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
