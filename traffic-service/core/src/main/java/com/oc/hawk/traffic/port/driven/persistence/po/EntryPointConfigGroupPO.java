package com.oc.hawk.traffic.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Table(name = "traffic_entrypoint_group")
@Entity
@DynamicUpdate
public class EntryPointConfigGroupPO extends BaseEntity {

    private String groupName;
    @CreatedDate
    private Timestamp createTime;
    @CreatedDate
    private Timestamp updateTime;

    public static EntryPointConfigGroupPO createBy(EntryPointConfigGroup group) {
        EntryPointConfigGroupPO groupPO = new EntryPointConfigGroupPO();
        groupPO.setGroupName(group.getGroupName());
        groupPO.setCreateTime(new Timestamp(new Date().getTime()));
        groupPO.setUpdateTime(new Timestamp(new Date().getTime()));
        return groupPO;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = new Timestamp(System.currentTimeMillis());
    }

    public EntryPointConfigGroup toEntryPointConfigGroup() {
        return EntryPointConfigGroup.builder()
            .groupId(new EntryPointGroupID(getId()))
            .groupName(groupName).build();
    }

}
