package com.oc.hawk.container;

import com.oc.hawk.common.spring.config.AsyncConfiguration;
import com.oc.hawk.common.spring.config.BaseConfiguration;
import com.oc.hawk.common.spring.config.WebConfiguration;
import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.container.domain.config.ContainerRuntimeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Import({WebConfiguration.class, BaseConfiguration.class, AsyncConfiguration.class})
@EnableScheduling
public class ContainerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContainerApplication.class, args);
    }

    @Bean
    @ConfigurationProperties("hawk.runtime")
    public ContainerRuntimeConfig containerRuntimeConfig() {
        return new ContainerRuntimeConfig();
    }

    @Bean
    @ConfigurationProperties("hawk.container")
    public ContainerConfiguration containerConfiguration() {
        return new ContainerConfiguration();
    }


    @Bean
    public RedisTemplate<String, Integer> integerRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

}

