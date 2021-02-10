package com.oc.hawk.container.domain.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
public class ContainerConfiguration {
    private String defaultInstanceNamespace;
    private String domainHost;
    private String buildNamespace;
    private String appImagePrefix;
    private String gatewayUrl;
    private Boolean pvcLog;

    public String getAppImagePrefix() {
        return StringUtils.endsWith(appImagePrefix, "/") ? appImagePrefix : appImagePrefix + "/";
    }
}
