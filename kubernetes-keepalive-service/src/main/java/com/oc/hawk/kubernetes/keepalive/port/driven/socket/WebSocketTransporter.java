package com.oc.hawk.kubernetes.keepalive.port.driven.socket;

import com.oc.hawk.kubernetes.keepalive.application.facade.Transporter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketTransporter implements Transporter {
    private final SimpMessagingTemplate socketTemplate;

    @Override
    public void send(String topic, WebSocketMessage msg) {
        socketTemplate.convertAndSend(topic, msg);
    }
}

