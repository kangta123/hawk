package com.oc.hawk.container.runtime.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Table(name = "container_service_configuration")
@Entity
@DynamicUpdate
public class InstanceConfigPO extends BaseEntity {
    private String serviceName;
    private String name;
    private String preStart;
    private String descn;
    private String namespace;

    private String performanceLevel;
    private String env;
    private String hosts;
    private String extraPorts;
    private String image;
    private String tag;
    private Integer innerPort;
    private String logPath;
    @Column(name = "health_check_path")
    private String healthCheckPath;
    private String property;
    private long projectId;
    private String branch;
    private Boolean debug = false;
    private String nginxLocation;
    private Boolean ssh = false;
    private Boolean jprofiler = false;
    private Boolean mesh = true;
    private String sshPassword;
    private String profile;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    @OneToMany(mappedBy = "config", cascade = CascadeType.ALL)
    private List<InstanceManagerPO> managers;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "volume_id")
    private InstanceVolumePO volume;
    private String projectType;

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

//    public void checkoutPermission() {
//        AccountHolder accountHolder = AccountHolderUtils.getAccountHolder();
//        if (accountHolder == null) {
//            return;
//        }
//        if (accountHolder.getDeptId() == 1L) {
//            return;
//        }
//        List<ServiceManager> managers = this.getManagers();
//        if (CollectionUtils.isEmpty(managers)) {
//            return;
//        }
//        if (managers.stream().anyMatch(m -> Objects.equals(m.getUserId(), accountHolder.getId()))) {
//            return;
//        }
//        throw new AppBusinessException("没有权限操作此服务");
//    }

}
