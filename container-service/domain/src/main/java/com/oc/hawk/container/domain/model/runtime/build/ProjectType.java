package com.oc.hawk.container.domain.model.runtime.build;

import com.oc.hawk.ddd.DomainValueObject;

@DomainValueObject
public class ProjectType {
    public final static String JAVA_SPRINGBOOT = "SPRINGBOOT";
    public final static String JAVA_TOMCAT = "TOMCAT";
    public final static String NGINX = "NGINX";
    public final static String JAVA = "JAVA";
    private final String runtimeType;
    private final String buildType;

    public ProjectType(String runtimeType, String buildType) {
        this.runtimeType = runtimeType;
        this.buildType = buildType;
    }

    public String getRuntimeType() {
        return runtimeType;
    }

    public boolean isTomcat() {
        return runtimeType.equalsIgnoreCase(JAVA_TOMCAT);
    }

    public boolean isJava() {
        return isTomcat() || isSpringBoot();
    }

    public boolean isSpringBoot() {
        return runtimeType.equalsIgnoreCase(JAVA_SPRINGBOOT);
    }

    public boolean isNginx() {
        return runtimeType.equalsIgnoreCase(NGINX);
    }

    public String getBuildType() {
        return buildType;
    }
}
