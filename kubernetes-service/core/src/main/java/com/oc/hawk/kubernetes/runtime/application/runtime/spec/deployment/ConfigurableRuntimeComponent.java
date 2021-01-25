package com.oc.hawk.kubernetes.runtime.application.runtime.spec.deployment;


public interface ConfigurableRuntimeComponent {
    void config(RuntimeComponent runtimeComponent);
}
