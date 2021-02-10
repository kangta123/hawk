package com.oc.hawk.container.domain.model.runtime.config;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author kangta123
 */
@Getter
@NoArgsConstructor
public class InstanceHealthCheck {
    private boolean enabled;
    private String path;

    public InstanceHealthCheck(Boolean enabled, String path) {
        this.enabled = Boolean.TRUE.equals(enabled);
        this.path = path;
    }

}
