package com.oc.hawk.common.spring.cloud.stream.event;

import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.common.utils.AccountHolderUtils;
import com.oc.hawk.ddd.event.DomainEvent;
import com.oc.hawk.ddd.event.EventIdGenerator;
import com.oc.hawk.ddd.event.EventPublisher;
import com.oc.hawk.ddd.event.UUIDEventIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class KafkaEventPublish implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EventIdGenerator eventIdGenerator = new UUIDEventIdGenerator();


    @Override
    public void publishDomainEvent(DomainEvent domainEvent) {
        publishEvent(KafkaTopic.DOMAIN_EVENT_TOPIC, domainEvent);
    }

    @Override
    public void publishEvent(String topic, DomainEvent domainEvent) {

        Assert.notNull(domainEvent.getType(), "事件类型不能为空");


        if (domainEvent.getId() == null) {
            domainEvent.withId(eventIdGenerator.id());
        }

        AccountHolder accountHolder = AccountHolderUtils.getAccountHolder();
        if (accountHolder != null) {
            domainEvent.withUser(accountHolder.getId());
        }

        kafkaTemplate.send(topic, domainEvent);

    }
}
