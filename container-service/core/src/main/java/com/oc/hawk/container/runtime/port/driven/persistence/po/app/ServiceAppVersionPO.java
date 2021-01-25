package com.oc.hawk.container.runtime.port.driven.persistence.po.app;

import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Getter
@Setter
@Table(name = "container_service_app_version")
@Entity
public class ServiceAppVersionPO extends BaseEntity {
    @ManyToOne
    private ServiceAppPO app;
    private String name;
    private Long configId;
    private Integer scale;
}
