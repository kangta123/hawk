package com.oc.hawk.kubernetes.keepalive.port.driven.socket;

public enum WebSocketTopic {
    WS_BUILD_TOPIC("/topic/build/job/%s"),
    WS_PROJECT_SERVICE_EVENT("/topic/project/%s/service/status");

    private String topic;

    WebSocketTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic(String key) {
        return String.format(topic, key);
    }
}
