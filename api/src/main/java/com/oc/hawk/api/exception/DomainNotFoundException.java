package com.oc.hawk.api.exception;


import com.oc.hawk.api.exception.error.CommonErrorCode;
import com.oc.hawk.api.exception.error.ErrorCode;

public class DomainNotFoundException extends AppBusinessException {

    private static final ErrorCode ERROR_CODE = CommonErrorCode.NOT_FOUND;

    public DomainNotFoundException() {
        super(ERROR_CODE.getCode(), ERROR_CODE.getStatus(), ERROR_CODE.getMessage());
    }

    public DomainNotFoundException(String name) {
        super(ERROR_CODE.getCode(), ERROR_CODE.getStatus(), ERROR_CODE.getMessage() + ", " + name);
    }

    public DomainNotFoundException(Long id) {
        super(ERROR_CODE.getCode(), ERROR_CODE.getStatus(), ERROR_CODE.getMessage() + ", " + id);
    }
}
