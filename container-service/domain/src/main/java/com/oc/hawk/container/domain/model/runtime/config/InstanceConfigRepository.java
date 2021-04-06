package com.oc.hawk.container.domain.model.runtime.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface InstanceConfigRepository {

    void delete(InstanceId id);

    List<InstanceConfig> byProject(Long projectId, String namespace);

    InstanceConfig byProject(String namespace, InstanceName instanceName);

    InstanceId save(InstanceConfig configuration);

    InstanceConfig byId(InstanceId id);

    InstanceId existed(String namespace, InstanceName name);

    Map<Long, Integer> countInstanceByProjects(Collection<Long> projectIds, String namespace);

    InstanceConfig byServiceName(String serviceName, String namespace);

    List<InstanceConfig> byProjectIds(List<Long> projectId);
}
