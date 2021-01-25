package com.oc.hawk.container.runtime.port.driving.event;

import org.springframework.context.ApplicationEvent;

public class ServiceAppCreatedEvent extends ApplicationEvent {
    private Long appId;


    public ServiceAppCreatedEvent(Object source, Long appId) {
        super(source);
        this.appId = appId;
    }

    public Long getAppId() {
        return appId;
    }
}
