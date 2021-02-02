package com.oc.hawk.kubernetes.runtime.application.runtime.spec.container;

import org.apache.commons.lang3.StringUtils;

public class VolumeMountPath {
    private static final String MULTI_PATH_SPLIT_FLAG = ":";
    String path;

    public VolumeMountPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }


    public String getVolumePath() {
        if (!path.contains(MULTI_PATH_SPLIT_FLAG)) {
            return null;
        }
        return StringUtils.substringBefore(path, MULTI_PATH_SPLIT_FLAG);
    }

    public String getMountPath() {
        if (!path.contains(MULTI_PATH_SPLIT_FLAG)) {
            return path;
        }
        return StringUtils.substringAfterLast(path, MULTI_PATH_SPLIT_FLAG);
    }
}
