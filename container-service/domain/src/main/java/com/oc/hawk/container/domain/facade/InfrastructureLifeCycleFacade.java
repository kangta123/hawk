package com.oc.hawk.container.domain.facade;

import com.oc.hawk.container.domain.model.app.ServiceApp;
import com.oc.hawk.container.domain.model.app.ServiceAppRule;
import com.oc.hawk.container.domain.model.app.ServiceAppVersion;
import com.oc.hawk.container.domain.model.project.ProjectRuntimeConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;

import java.util.List;
import java.util.Map;

public interface InfrastructureLifeCycleFacade {


    void start(InstanceConfig config, ProjectRuntimeConfig runtimeConfig);
    void stop(InstanceConfig config);

    void scale(ServiceAppVersion version);

    void applyServiceAppRules(ServiceApp app, List<String> versionNames, List<ServiceAppRule> appRules);

    Map<Long, Integer> countRuntimeByProject(String namespace);
}
