package com.oc.hawk.common.spring.config;

import com.oc.hawk.common.spring.ApplicationConstants;
import com.oc.hawk.common.spring.ApplicationContextHolder;
import com.oc.hawk.common.spring.config.feign.FeignPageableQueryEncoder;
import com.oc.hawk.common.spring.config.feign.FeignRequestInterceptor;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@Configuration
@ComponentScan(basePackages = {"com.oc.hawk"}, excludeFilters = {@ComponentScan.Filter(IgnoreScan.class)})
@EnableFeignClients(basePackages = {"com.oc.hawk.**.service.gateway", "com.oc.hawk.**.facade.feign"})
@EnableDiscoveryClient
@Import(value = {KafkaConfiguration.class, Jsr310JpaConverters.class})
@Slf4j
public class BaseConfiguration {
    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return ApplicationContextHolder.getInstance();
    }

    @Bean
    @ConditionalOnClass({RequestInterceptor.class, Feign.class})
    feign.RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }

    @Bean
    public Encoder feignEncoder() {
        return new FeignPageableQueryEncoder();
    }


    @Bean
    public ApplicationConstants applicationConstants() {
        return new ApplicationConstants();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
