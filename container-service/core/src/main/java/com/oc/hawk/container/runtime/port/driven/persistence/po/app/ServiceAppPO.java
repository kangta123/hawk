package com.oc.hawk.container.runtime.port.driven.persistence.po.app;

import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "container_service_app")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class ServiceAppPO extends BaseEntity {
    private Long projectId;
    private String name;
    private String descn;
    private String app;
    private String namespace;

}
