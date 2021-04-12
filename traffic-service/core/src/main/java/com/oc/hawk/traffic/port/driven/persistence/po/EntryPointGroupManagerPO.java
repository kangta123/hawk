package com.oc.hawk.traffic.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

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
    
}
