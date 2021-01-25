package com.oc.hawk.api.exception;


import com.oc.hawk.api.exception.error.Error;

public class RemoteCallException extends AppBusinessException {

    private static final long serialVersionUID = 2705858920513438691L;
    private Error originError;

    public RemoteCallException(Error error, int httpStatus) {
        super(error.getCode(), httpStatus, error.getMessage());
        this.originError = error;
    }

    public Error getOriginError() {
        return originError;
    }

}
