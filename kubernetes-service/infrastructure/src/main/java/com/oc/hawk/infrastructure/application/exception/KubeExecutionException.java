package com.oc.hawk.infrastructure.application.exception;

import com.oc.hawk.api.exception.BaseException;

public class KubeExecutionException extends BaseException {
    public KubeExecutionException(String message) {
        super(message);
    }
}
