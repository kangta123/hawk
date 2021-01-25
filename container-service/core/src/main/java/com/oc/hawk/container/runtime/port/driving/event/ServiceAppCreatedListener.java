package com.oc.hawk.container.runtime.port.driving.event;

import com.oc.hawk.container.domain.model.runtime.config.BaseInstanceConfig;
import com.oc.hawk.container.runtime.application.app.service.ServiceAppRuleManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceAppCreatedListener implements ApplicationListener<ServiceAppCreatedEvent> {
    private final ServiceAppRuleManager serviceAppRuleManager;

    @Override
    public void onApplicationEvent(ServiceAppCreatedEvent serviceAppCreatedEvent) {
        serviceAppRuleManager.applyDefaultAppRuleIfEmpty((BaseInstanceConfig) serviceAppCreatedEvent.getSource(), serviceAppCreatedEvent.getAppId());
    }
}
