package com.oc.hawk.kubernetes.keepalive.application.facade;

import com.oc.hawk.kubernetes.keepalive.port.driven.socket.WebSocketMessage;

public interface Transporter {
    void send(String topic, WebSocketMessage msg);

}
