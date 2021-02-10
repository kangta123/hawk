package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.model.runtime.config.volume.InstanceVolume;
import com.oc.hawk.container.domain.model.runtime.config.volume.SharedInstanceVolume;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@DomainValueObject
@Getter
public class InstanceLog {
    public final static String LOG_PVC_NAME = "hawk-log";

    private final String logPath;

    public InstanceLog(String logPath) {
        this.logPath = logPath;
    }

    public InstanceVolume getLogVolume(String serviceName) {
        if (StringUtils.isNotEmpty(logPath)) {
            return new SharedInstanceVolume(LOG_PVC_NAME, logPath, serviceName);
        } else {
            return null;
        }
    }
}
