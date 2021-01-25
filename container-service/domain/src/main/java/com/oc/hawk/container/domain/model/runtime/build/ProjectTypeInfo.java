package com.oc.hawk.container.domain.model.runtime.build;

import com.oc.hawk.ddd.DomainValueObject;

@DomainValueObject
public class ProjectTypeInfo {
    public final static String JAVA_SPRINGBOOT = "SPRINGBOOT";
    public final static String JAVA_TOMCAT = "TOMCAT85";
    public final static String NGINX = "NGINX";
    public final static String JAVA = "JAVA";
    private final String projectType;

    public ProjectTypeInfo(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectType() {
        return projectType;
    }

    public boolean isTomcat() {
        return projectType.equalsIgnoreCase(JAVA_TOMCAT);
    }

    public boolean isJava() {
        return isTomcat() || isSpringBoot();
    }

    public boolean isSpringBoot() {
        return projectType.equalsIgnoreCase(JAVA_SPRINGBOOT);
    }

    public boolean isNginx() {
        return projectType.equalsIgnoreCase(NGINX);
    }
}
