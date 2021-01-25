package com.oc.hawk.kubernetes.keepalive.port.driven.socket;

import com.oc.hawk.container.api.event.EntryPointUpdatedEvent;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import com.oc.hawk.kubernetes.api.dto.RuntimeStartFailedDto;
import com.oc.hawk.project.api.dto.ProjectBuildJobDTO;
import lombok.Data;

@Data
public class WebSocketMessage {
    private WebSocketType type;
    private Object data;

    private WebSocketMessage(WebSocketType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public static WebSocketMessage text(String msg) {
        return new WebSocketMessage(WebSocketType.TEXT, msg);
    }

    public static WebSocketMessage buildJob(ProjectBuildJobDTO projectBuildJob) {
        return new WebSocketMessage(WebSocketType.BUILD_JOB, projectBuildJob);
    }


    public static WebSocketMessage entryPoint(EntryPointUpdatedEvent entryPointUpdatedEvent) {
        return new WebSocketMessage(WebSocketType.ENTRYPOINT, entryPointUpdatedEvent);
    }

    public static WebSocketMessage runtimeFailed(RuntimeStartFailedDto data) {
        return new WebSocketMessage(WebSocketType.RUNTIME_FAILED, data);
    }

    public static WebSocketMessage runtime(RuntimeInfoDTO runtimeInfoDTO) {
        return new WebSocketMessage(WebSocketType.RUNTIME_INFO, runtimeInfoDTO);
    }

    @Override
    public String toString() {
        return type + " = " + data;
    }
}
