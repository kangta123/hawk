package com.oc.hawk.kubernetes.runtime.application.runtime.starter;

import com.oc.hawk.common.spring.ApplicationContextHolder;
import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KubernetesServiceStarterFactory {

    public static ServiceRuntimeStarter starter(RuntimeConfigSpec configuration) {
        if (configuration.isBusiness()) {
            return ApplicationContextHolder.getBean(BusinessRuntimeStarter.class);
        } else {
            return ApplicationContextHolder.getBean(NormalRuntimeStarter.class);
        }
    }

}



