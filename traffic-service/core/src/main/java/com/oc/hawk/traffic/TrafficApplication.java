package com.oc.hawk.traffic;

import com.oc.hawk.common.spring.config.BaseConfiguration;
import com.oc.hawk.common.spring.config.WebConfiguration;
import com.oc.hawk.traffic.entrypoint.domain.config.TrafficTraceHeaderFilterConfig;
import com.oc.hawk.traffic.port.driven.facade.excutor.NoErrorResultHandler;
import com.oc.hawk.traffic.port.driven.facade.excutor.RestTemplateProxy;
import com.oc.hawk.traffic.port.driving.event.ApplicationStartupListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Import(value = {WebConfiguration.class, BaseConfiguration.class})
@Slf4j
@EnableScheduling
public class TrafficApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TrafficApplication.class);
        springApplication.run(args);
        springApplication.addListeners(new ApplicationStartupListener());
    }
    
    @Bean
    public RestTemplate customRestTemplate() {
        RestTemplate restTemplate = new RestTemplateProxy();
        restTemplate.setErrorHandler(new NoErrorResultHandler());
        return restTemplate;
    }
    
    @Bean
    @ConfigurationProperties(prefix = "trace.header-filter")
    public TrafficTraceHeaderFilterConfig trafficTraceHeaderFilterConfig() {
        return new TrafficTraceHeaderFilterConfig();
    }
}

