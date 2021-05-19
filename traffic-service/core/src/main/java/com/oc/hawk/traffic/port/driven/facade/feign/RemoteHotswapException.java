package com.oc.hawk.traffic.port.driven.facade.feign;

import com.oc.hawk.api.exception.AppBusinessException;

/**
 * @author kangta123
 */
public class RemoteHotswapException extends AppBusinessException {
    public RemoteHotswapException(String message) {
        super(message);
    }
}
