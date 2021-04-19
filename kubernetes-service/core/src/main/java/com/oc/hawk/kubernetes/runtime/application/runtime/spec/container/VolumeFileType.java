package com.oc.hawk.kubernetes.runtime.application.runtime.spec.container;

/**
 * @author kangta123
 */
public enum VolumeFileType {
    File, Socket;

    public static VolumeFileType withDefault(String type) {
        if (type == null) {
            return VolumeFileType.File;
        }
        return VolumeFileType.valueOf(type);
    }
}
