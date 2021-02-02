package com.oc.hawk.ddd.event;

public interface EventPublisher {
    void publishDomainEvent(DomainEvent domainEvent);

    void publishEvent(String topic, DomainEvent domainEvent);

}
