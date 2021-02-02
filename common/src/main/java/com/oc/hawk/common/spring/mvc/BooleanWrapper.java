package com.oc.hawk.common.spring.mvc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BooleanWrapper {
    public static BooleanWrapper TRUE = new BooleanWrapper(true);
    public static BooleanWrapper FALSE = new BooleanWrapper(false);
    private final boolean success;
    private final Object message;

    public BooleanWrapper(boolean success) {
        this(success, null);
    }

    @JsonCreator
    public BooleanWrapper(
        @JsonProperty("success") boolean success,
        @JsonProperty("message") Object message) {
        this.success = success;
        this.message = message;
    }

    public static BooleanWrapper of(boolean status) {
        return status ? TRUE : FALSE;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getMessage() {
        return message;
    }

}
