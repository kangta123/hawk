package com.oc.hawk.api.exception;


import com.oc.hawk.api.exception.error.CommonErrorCode;
import com.oc.hawk.api.exception.error.ErrorCode;

public class ServiceUnavailableException extends AppBusinessException {

    private static final ErrorCode ERROR_CODE = CommonErrorCode.SERVICE_UNAVAILABLE;

    public ServiceUnavailableException(String message) {
        super(ERROR_CODE.getCode(), ERROR_CODE.getStatus(), " 远程服务不可用: " + message);
    }

}
