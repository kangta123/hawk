package com.oc.hawk.monitor.port.driven.facade;

/**
 * @author kangta123
 */
public interface KubernetesFacade {

    String getAvailablePodName(String namespace, String app, String version, String name);
}
