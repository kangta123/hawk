package com.oc.hawk.kubernetes.runtime.application.runtime.starter;


import com.oc.hawk.kubernetes.runtime.application.runtime.spec.RuntimeConfigSpec;

public interface RuntimeStarter {
    void start(RuntimeConfigSpec configuration);
}
