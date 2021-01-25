package com.oc.hawk.container.domain.model.runtime.config.exception;

import com.oc.hawk.api.exception.AppBusinessException;

public class InstanceConfigCreateIllegalArgumentException extends AppBusinessException {
    public InstanceConfigCreateIllegalArgumentException(String message) {
        super(message);
    }
}
