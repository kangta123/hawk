package com.oc.hawk.kubernetes.domain.model;

import com.oc.hawk.api.exception.BaseException;

public class RuntimeRestartCountExceededException extends BaseException {
    public RuntimeRestartCountExceededException() {
        super("");
    }
}
