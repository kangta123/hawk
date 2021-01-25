package com.oc.hawk.container.domain.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContainerConfiguration {
    private String defaultInstanceNamespace;
    private String domainHost;
    private String buildNamespace;
    private String appImagePrefix;
    private String gatewayUrl;
    private Boolean pvcLog;
}
