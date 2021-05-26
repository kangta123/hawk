package com.oc.hawk.jvm.port.driven.facade;

import com.oc.hawk.api.exception.AppBusinessException;

/**
 * @author kangta123
 */
public class RemoteHotswapException extends AppBusinessException {
    public RemoteHotswapException(String message) {
        super(message);
    }
}
