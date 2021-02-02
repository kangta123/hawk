package com.oc.hawk.project.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "base_project_user")
@Entity
public class ProjectSharedPO extends BaseEntity {
    private Long project;
    private Long user;
    private LocalDateTime time;
}
