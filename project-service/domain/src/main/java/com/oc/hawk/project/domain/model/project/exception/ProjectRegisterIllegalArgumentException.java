package com.oc.hawk.project.domain.model.project.exception;

import com.oc.hawk.api.exception.AppBusinessException;

public class ProjectRegisterIllegalArgumentException extends AppBusinessException {
    public ProjectRegisterIllegalArgumentException(String message) {
        super(message);
    }
}
