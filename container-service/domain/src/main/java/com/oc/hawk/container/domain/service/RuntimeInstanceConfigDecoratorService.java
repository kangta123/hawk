package com.oc.hawk.container.domain.service;

import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.NginxInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.SpringBootInstanceConfig;
import com.oc.hawk.container.domain.service.decorator.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RuntimeInstanceConfigDecoratorService implements InstanceConfigDecoratorFacade {

    private final ContainerConfiguration containerConfiguration;

    @Override
    public void assemble(InstanceConfig config) {
        InstanceConfigRuntimeDecorator runtimeDecorator = new BaseInstanceConfigRuntimeDecorator(containerConfiguration);
        runtimeDecorator = new AppVolumeDecorator(runtimeDecorator);
        runtimeDecorator = new HostRemoteAccessDecorator(runtimeDecorator);
        runtimeDecorator = new InstanceLogDecorator(runtimeDecorator, containerConfiguration);

        if (config instanceof NginxInstanceConfig) {
            runtimeDecorator = new NginxDecorator(runtimeDecorator, containerConfiguration);
        } else {
            runtimeDecorator = new JavaJprofilerDecorator(runtimeDecorator);
            runtimeDecorator = new JavaRemoteDebugDecorator(runtimeDecorator);
            runtimeDecorator = new JavaPropertyDecorator(runtimeDecorator);
            runtimeDecorator = new EurekaInstanceConfigDecorator(runtimeDecorator);

            if (config instanceof SpringBootInstanceConfig) {
                runtimeDecorator = new SpringBootDecorator(runtimeDecorator);
            }
        }

        runtimeDecorator.config(config);
    }
}
