package com.oc.hawk.project.domain.model.buildjob;

import com.oc.hawk.ddd.DomainValueObject;
import org.apache.commons.lang3.StringUtils;

@DomainValueObject
public class ProjectImageApp {
    private final String image;

    public ProjectImageApp(String image) {
        this.image = image;
    }

    public String getApp() {
        if (StringUtils.isEmpty(image)) {
            return null;
        }
        return StringUtils.substringAfterLast(image, "/");
    }
}
