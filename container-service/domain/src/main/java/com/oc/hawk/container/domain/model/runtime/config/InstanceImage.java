package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.config.ContainerConfiguration;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@DomainValueObject
@Getter
public class InstanceImage {
    private final String SPLIT_SIGN = ":";
    private final String app;
    private final String tag;
    private final String branch;
    private final String BACKSLASH = "/";

    public InstanceImage(String app, String tag, String branch) {
        this.app = app;
        this.tag = tag;
        this.branch = branch;
    }

    public String getApp() {
        if (app.contains(BACKSLASH)) {
            return StringUtils.substringAfterLast(app, BACKSLASH);
        }
        return app;
    }

    public String getFullImage(ContainerConfiguration containerConfiguration) {
        return containerConfiguration.getAppImagePrefix() + app + SPLIT_SIGN + tag;
    }

}
