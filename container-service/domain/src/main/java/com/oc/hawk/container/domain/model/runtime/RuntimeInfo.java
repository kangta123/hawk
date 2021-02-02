package com.oc.hawk.container.domain.model.runtime;

import lombok.Getter;

@Getter
public class RuntimeInfo {
    private final int restartCount;
    private final int MAX_FAILURE_TIMES = 3;
    private final String name;

    public RuntimeInfo(int restartCount, String name) {
        this.restartCount = restartCount;
        this.name = name;
    }

    public void checkRuntimeInfo() {
        if (restartCount > MAX_FAILURE_TIMES) {
            throw new ExcessiveRestartTimeException("服务启动失败, 尝试重启超过最大次数");
        }
    }
}
