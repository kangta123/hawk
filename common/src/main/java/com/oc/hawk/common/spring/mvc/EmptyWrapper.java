package com.oc.hawk.common.spring.mvc;

import java.io.Serializable;

public class EmptyWrapper implements Serializable {
    public static EmptyWrapper empty = new EmptyWrapper();

    private EmptyWrapper() {
    }
}
