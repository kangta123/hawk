package com.oc.hawk.base.domain.model.user.exception;

import com.oc.hawk.api.exception.AppBusinessException;

public class UserRegisterInvalidParamException extends AppBusinessException {
    public UserRegisterInvalidParamException(String message) {
        super(message);
    }
}
