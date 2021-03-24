package com.oc.hawk.traffic.port.driven.persistence.po;

import com.google.common.base.Joiner;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Table(name = "traffic_entrypoint_group_manager")
@Entity
@DynamicUpdate
public class EntryPointGroupManagerPO extends BaseEntity {

    private Long userId;
    private String groupids;

    @CreatedDate
    private Timestamp createTime;
    @CreatedDate
    private Timestamp updateTime;

    public static EntryPointGroupManagerPO createBy(Long userId, List<EntryPointGroupID> entryPointGroupIDList) {
        EntryPointGroupManagerPO po = new EntryPointGroupManagerPO();
        po.setUserId(userId);
        String groupids = Joiner.on(",").join(entryPointGroupIDList);
        po.setGroupids(groupids);
        return po;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = new Timestamp(System.currentTimeMillis());
    }

}
