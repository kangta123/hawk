package com.oc.hawk.infrastructure.application.exception;

import com.oc.hawk.api.exception.AppBusinessException;

public class PodStartFailedException extends AppBusinessException {
    public PodStartFailedException(String message) {
        super(message);
    }
}
