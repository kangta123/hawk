package com.oc.hawk.common.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
@IgnoreScan
public class WebSocketBrokerConfiguration implements WebSocketMessageBrokerConfigurer {

    public static final String BROKER_TOPIC_PREFIX = "/topic";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS().setClientLibraryUrl("https://cdn.jsdelivr.net/npm/sockjs-client@1.3.0/dist/sockjs.min.js");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        // Enables a simple in-memory broker
        registry.enableSimpleBroker(BROKER_TOPIC_PREFIX);
    }

}

