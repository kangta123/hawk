package com.oc.hawk.infrastructure.application.exception;

import com.oc.hawk.api.exception.AppBusinessException;

public class PodNotReadyException extends AppBusinessException {
    public PodNotReadyException(String message) {
        super(message);
    }
}
