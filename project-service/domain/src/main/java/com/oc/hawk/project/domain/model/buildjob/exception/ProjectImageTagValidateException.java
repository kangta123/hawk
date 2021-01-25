package com.oc.hawk.project.domain.model.buildjob.exception;

import com.oc.hawk.api.exception.AppBusinessException;

public class ProjectImageTagValidateException extends AppBusinessException {
    public ProjectImageTagValidateException(String message) {
        super(message);
    }
}
