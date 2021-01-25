package com.oc.hawk.api.exception;


import com.oc.hawk.api.exception.error.CommonErrorCode;
import com.oc.hawk.api.exception.error.ErrorCode;

public class AppBusinessException extends BaseException {

	private static final long serialVersionUID = 8299312292335394970L;

	private static final ErrorCode DEFAULT_CODE = CommonErrorCode.INTERNAL_ERROR;

    private String code = DEFAULT_CODE.getCode();

    private int httpStatus = DEFAULT_CODE.getStatus();

    public AppBusinessException(String code, int httpStatus, String message) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public AppBusinessException(String message) {
        super(message);
    }

    /**
     * @param errorCode 状态码, 这个字段会在错误信息里返回给客户端.
     * @param message
     */
    public AppBusinessException(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), errorCode.getStatus(), message);
    }

    public AppBusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public String getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
