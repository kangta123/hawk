package com.oc.hawk.kubernetes.keepalive.application.client;

import com.oc.hawk.container.domain.model.build.protocal.ProjectBuildMessageHandler;
import io.fabric8.kubernetes.client.Callback;

public class ProjectBuildCallBack implements Callback<byte[]> {
    private final ProjectBuildMessageHandler protocol;


    public ProjectBuildCallBack(ProjectBuildMessageHandler protocol) {
        this.protocol = protocol;
    }

    @Override
    public void call(byte[] input) {
        protocol.append(new String(input));
    }
}
