package com.oc.hawk.traffic.port.driven.persistence.po;

import com.google.common.base.Joiner;
import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointGroupID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Table(name = "traffic_entrypoint_group_manager")
@Entity
@DynamicUpdate
public class EntryPointGroupManagerPO extends BaseEntity {

    private Long userId;
    private String groupids;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static EntryPointGroupManagerPO createBy(Long userId, List<EntryPointGroupID> entryPointGroupIdList) {
        EntryPointGroupManagerPO po = new EntryPointGroupManagerPO();
        po.setUserId(userId);
        String groupids = Joiner.on(",").join(entryPointGroupIdList);
        po.setGroupids(groupids);
        return po;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

}
