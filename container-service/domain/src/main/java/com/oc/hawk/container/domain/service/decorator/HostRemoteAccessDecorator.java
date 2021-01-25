package com.oc.hawk.container.domain.service.decorator;

import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceConfig;
import com.oc.hawk.container.domain.model.runtime.config.InstanceHost;

import java.util.Objects;

public class HostRemoteAccessDecorator extends AbstractInstanceConfigDecorator {
    private static final String ENV_SSH_KEY = "SSH";
    private static final String ENV_SSH_PASSWORD_KEY = "SSHPASSWORD";

    public HostRemoteAccessDecorator(InstanceConfigRuntimeDecorator configRuntimeDecorator) {
        super(configRuntimeDecorator);
    }

    @Override
    public void config(InstanceConfig instanceConfig) {
        super.config(instanceConfig);

        BaseInstanceConfig baseConfig = (BaseInstanceConfig) instanceConfig.getBaseConfig();
        InstanceHost host = baseConfig.getHost();
        if (Objects.nonNull(host.getRemoteAccess())) {
            baseConfig.addEnv(ENV_SSH_KEY, ENABLED);
            baseConfig.addEnv(ENV_SSH_PASSWORD_KEY, host.getRemoteAccess().getSshPassword());
        } else {
            baseConfig.addEnv(ENV_SSH_KEY, DISABLED);
        }
    }
}
