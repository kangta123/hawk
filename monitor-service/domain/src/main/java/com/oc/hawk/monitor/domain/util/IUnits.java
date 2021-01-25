package com.oc.hawk.monitor.domain.util;

/**
 * @author kangta123
 */
public interface IUnits {
    public String format(long size, String pattern);
    public long getUnitSize();
}

