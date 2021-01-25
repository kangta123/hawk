package com.oc.hawk.kubernetes.runtime.application.runtime.spec.container.exception;

import com.oc.hawk.api.exception.AppBusinessException;

public class ServiceVolumeNameIllegalException extends AppBusinessException {
    public ServiceVolumeNameIllegalException(String message) {
        super(message);
    }
}
