package com.oc.hawk.base.domain.model.auth.exception;


import com.oc.hawk.api.exception.AppBusinessException;
import com.oc.hawk.api.exception.error.CommonErrorCode;
import com.oc.hawk.api.exception.error.ErrorCode;

public class InvalidUsernameOrPasswordException extends AppBusinessException {
    private static final ErrorCode ERROR_CODE = CommonErrorCode.NOT_FOUND;

    public InvalidUsernameOrPasswordException(String message) {
        super(ERROR_CODE.getCode(), ERROR_CODE.getStatus(), message);
    }
}
