package com.oc.hawk.container.runtime.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfig;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeVolumeConfig;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "container_project_runtime_config")
@Getter
@Setter
public class ProjectRuntimeConfigPO extends BaseEntity {

    private String buildType;
    private String runtimeType;
    private String volume;
    private String mountPath;
    private String buildImage;
    private String dataImage;
    private String dataSharedVolume;
    private String appImage;

    public ProjectRuntimeConfig toConfig(){
        ProjectRuntimeVolumeConfig projectRuntimeVolumeConfig = new ProjectRuntimeVolumeConfig(getVolume(), getMountPath());
        return new ProjectRuntimeConfig(getBuildImage(), getDataImage(), getAppImage(), projectRuntimeVolumeConfig);
    }
}
