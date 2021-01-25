package com.oc.hawk.kubernetes.client;

import com.oc.hawk.common.spring.cloud.stream.event.KafkaEventPublish;
import com.oc.hawk.ddd.event.DomainEvent;
import io.fabric8.kubernetes.client.Callback;
import lombok.RequiredArgsConstructor;

import static com.oc.hawk.api.constant.KafkaTopic.RUNTIME_OUTPUT_TOPIC;

@RequiredArgsConstructor
public class EventPodCallBack implements Callback<byte[]> {
    private final KafkaEventPublish kafkaDomainEventPublish;
    private final long domainId;

    @Override
    public void call(byte[] input) {
        kafkaDomainEventPublish.publishEvent(RUNTIME_OUTPUT_TOPIC, DomainEvent.byData(domainId, "RECEIVED", new String(input)));
    }
}
