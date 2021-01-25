package com.oc.hawk.container.domain.model.runtime;

import com.oc.hawk.api.exception.BaseException;

public class ExcessiveRestartTimeException extends BaseException {
    public ExcessiveRestartTimeException(String message) {
        super(message);
    }
}
