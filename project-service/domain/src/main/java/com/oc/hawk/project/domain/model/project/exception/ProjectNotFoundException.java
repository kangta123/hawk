package com.oc.hawk.project.domain.model.project.exception;

import com.oc.hawk.api.exception.AppBusinessException;
import com.oc.hawk.api.exception.error.CommonErrorCode;

public class ProjectNotFoundException extends AppBusinessException {
    public ProjectNotFoundException() {
        super(CommonErrorCode.NOT_FOUND, "项目不存在");
    }
}
