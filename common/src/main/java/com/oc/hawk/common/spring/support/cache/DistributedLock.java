package com.oc.hawk.common.spring.support.cache;

import java.io.Closeable;

public interface DistributedLock extends Closeable {
    boolean lock();

    boolean tryLock();

    void unlock();

    @Override
    void close();
}

