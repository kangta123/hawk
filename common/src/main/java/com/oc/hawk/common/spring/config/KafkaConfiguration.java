package com.oc.hawk.common.spring.config;

import com.oc.hawk.api.constant.KafkaTopic;
import com.oc.hawk.common.spring.ApplicationConstants;
import com.oc.hawk.api.utils.JsonUtils;
import com.oc.hawk.ddd.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.List;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
@Slf4j
@IgnoreScan
public class KafkaConfiguration {
    private final KafkaProperties properties;
    private final ApplicationConstants applicationConstants;
    private List<String> kafkaTopics = KafkaTopic.getTopics();

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(4);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.setRecordInterceptor(domainEventRecordInterceptor());
        return factory;
    }

    @Bean
    public ProducerFactory<?, ?> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = properties.buildProducerProperties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = properties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, applicationConstants.getAppName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }


    public RecordInterceptor<String, Object> domainEventRecordInterceptor() {
        return record -> {
            if (kafkaTopics.contains(record.topic())) {
                Object value = record.value();
                if (value instanceof DomainEvent) {
                    DomainEvent domainEvent = (DomainEvent) value;
                    Object data = domainEvent.getData();
                    if (data == null) {
                        return record;
                    }
                    if (data instanceof Map) {
                        try {
                            Class<?> cls = Class.forName(domainEvent.getClz());
                            Object o = JsonUtils.map2Object((Map) data, cls);
                            domainEvent.resetData(o);
                        } catch (ClassNotFoundException e) {
                            return record;
                        } catch (Exception e) {
                            log.info("Cannot deserializer event data.", e);
                        }
                    } else {
                        log.error("Invalidate the format of domain event value. {}", data.getClass());
                    }
                }
            }

            return record;
        };
    }
}

