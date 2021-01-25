package com.oc.hawk.container.domain.model.runtime.info;

import org.apache.commons.lang3.StringUtils;

public enum RuntimeCatalog {
    BUSINESS, BUILD;

    public static RuntimeCatalog getCatalog(String runtimeCatalog) {
        if (StringUtils.isNotEmpty(runtimeCatalog)) {
            return RuntimeCatalog.valueOf(runtimeCatalog.toUpperCase());
        } else {
            return null;
        }
    }

    public static boolean is(String runtimeCatalog, RuntimeCatalog catalog) {
        return RuntimeCatalog.valueOf(runtimeCatalog.toUpperCase()) == catalog;
    }
}
