package com.oc.hawk.container.runtime.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "container_service_volume")
@Entity
public class InstanceVolumePO extends BaseEntity {
    private String mountPath;
    private String volumeName;
}
