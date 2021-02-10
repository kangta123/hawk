package com.oc.hawk.container.domain.model.runtime.config;

import com.google.common.collect.Sets;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.model.runtime.SystemServicePort;
import com.oc.hawk.container.domain.model.runtime.build.ProjectType;
import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.NormalInstanceVolume;
import com.oc.hawk.container.domain.model.runtime.info.PerformanceLevel;
import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Builder
@DomainEntity
public class BaseInstanceConfig implements InstanceConfig {

    private final InstanceId id;
    private final InstanceName name;
    private final Long projectId;
    private final InstanceNetwork network;
    private final Set<InstanceVolume> volumes;
    private final String namespace;
    private final LocalDateTime updatedTime;
    private final LocalDateTime createdTime;
    private InstanceHost host;
    private String descn;
    private PerformanceLevel performanceLevel;
    private InstanceHealthCheck healthCheck;
    private InstanceLog log;
    private InstanceImage image;
    private List<InstanceManager> managers;

    @Override
    public InstanceConfig getBaseConfig() {
        return this;
    }

    @Override
    public String getRuntimeType() {
        return ProjectType.JAVA;
    }


    public void update(String descn, String performanceLevel) {
        if (descn != null) {
            this.descn = descn;
        }
        if (performanceLevel != null) {
            this.performanceLevel = PerformanceLevel.valueOf(performanceLevel);
        }
    }

    public void updateNetwork(Boolean mesh, List<Integer> exposePorts) {
        this.network.updateInstanceEntryPont(mesh, exposePorts);
    }

    public void createInstanceHost(String hosts, String preStart, InstanceRemoteAccess instanceRemoteAccess, Map<String, String> env) {
        this.host = InstanceHost.builder()
            .preStart(preStart)
            .env(env)
            .hosts(hosts)
            .remoteAccess(instanceRemoteAccess)
            .build();
        if (instanceRemoteAccess != null) {
            exposePort(SystemServicePort.SSH_PORT);
        }
    }

    public void createOrUpdateInstanceHost(String hosts, String preStart, Boolean ssh, Map<String, String> env) {
        if (this.host == null) {
            if (Objects.equals(ssh, Boolean.TRUE)) {
                InstanceRemoteAccess remoteAccess = InstanceRemoteAccess.createNew();
                network.exposePort(SystemServicePort.SSH_PORT);
                this.createInstanceHost(hosts, preStart, remoteAccess, env);
            }
        } else {
            this.host.update(hosts, preStart, ssh, env);
        }

    }

    public void updateInstanceVolume(String volumeName, String mountPath) {

        InstanceVolume volume = new NormalInstanceVolume(volumeName, mountPath);
        this.volumes.remove(volume);
        if (StringUtils.isNotEmpty(mountPath)) {
            this.volumes.add(volume);
        }
    }

    public void updateInstanceLog(String logPath) {
        if (logPath != null) {
            log = new InstanceLog(logPath);
        }
    }

    public void updateInstanceManagers(List<InstanceManager> managers) {
        if (managers != null) {
            this.managers = managers;
        }
    }

    public void updateImage(InstanceImage image) {
        this.image = image;
    }

    public void addEnv(String key, String value) {
        this.getHost().addEnv(key, value);
    }

    public void addVolume(InstanceVolume instanceVolume) {
        Set<InstanceVolume> volumes = this.getVolumes();
        if (volumes == null) {
            volumes = Sets.newHashSet();
        }
        volumes.add(instanceVolume);
    }

    public String getDomainHost(ContainerConfiguration configuration) {
        return new InstanceDomain(this.getNetwork().getServiceName(), this.getNamespace()).getDomain(configuration);
    }

    public void exposePort(SystemServicePort port) {
        network.exposePort(port);
    }

    public void updateExposePort(Map<Integer, Integer> exposePoints) {
        exposePoints.forEach(network::updateExposedPort);
    }

    public InstanceImage getImage() {
        return image;
    }

    public void updateNewVersion(InstanceImageVersion version) {
        final InstanceImage newImage = version.getInstanceImage(getImage().getApp());
        this.updateImage(newImage);
    }


    public void updateHealthCheck(Boolean enable, String path) {
        if (enable != null && path != null) {
            healthCheck = new InstanceHealthCheck(enable, path);
        }
    }
}


