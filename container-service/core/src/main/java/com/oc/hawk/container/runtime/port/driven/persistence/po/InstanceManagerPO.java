package com.oc.hawk.container.runtime.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "container_service_manager")
@Entity
public class InstanceManagerPO extends BaseEntity {
    private String username;
    private Long userId;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private InstanceConfigPO config;
}
