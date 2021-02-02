package com.oc.hawk.project.domain.model.project.exception;

import com.oc.hawk.api.exception.BaseException;

public class CodeBasePasswordDecodeException extends BaseException {
    public CodeBasePasswordDecodeException(String message) {
        super(message);
    }

    public CodeBasePasswordDecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
