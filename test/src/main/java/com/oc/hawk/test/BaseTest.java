package com.oc.hawk.test;

import com.oc.hawk.api.utils.BeanUtils;

import java.time.LocalDateTime;

public abstract class BaseTest {
    protected String str() {
        return TestHelper.anyString();
    }

    protected int integer() {
        return TestHelper.anyInt();
    }

    protected long along() {
        return TestHelper.anyLong();
    }

    protected <T> T newInstance(Class<T> cls) {
        return TestHelper.newInstance(cls);
    }

    protected <T> T newInstance(Class<T> cls, String property, Object value) {
        final T instance = newInstance(cls);
        this.setObjectValue(instance, property, value);
        return instance;
    }

    protected LocalDateTime datetime() {
        return newInstance(LocalDateTime.class);
    }

    protected void setObjectValue(Object obj, String property, Object value) {
        BeanUtils.setProperty(obj, property, value);
    }
}
