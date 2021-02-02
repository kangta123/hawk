package com.oc.hawk.container.runtime.application.representation;

import com.google.common.base.Objects;
import com.oc.hawk.container.api.dto.InstanceConfigDTO;
import com.oc.hawk.kubernetes.api.constants.RuntimeInfoDTO;
import lombok.Data;

@Data
public class RuntimeInfoEntryKey {
    private String name;
    private String namespace;

    public RuntimeInfoEntryKey(String name, String namespace) {
        this.name = name;
        this.namespace = namespace;
    }

    public static RuntimeInfoEntryKey create(RuntimeInfoDTO info) {
        return new RuntimeInfoEntryKey(info.getName(), info.getNamespace());
    }

    public static RuntimeInfoEntryKey create(InstanceConfigDTO info) {
        return new RuntimeInfoEntryKey(info.getName(), info.getNamespace());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RuntimeInfoEntryKey that = (RuntimeInfoEntryKey) o;
        return Objects.equal(name, that.name) &&
            Objects.equal(namespace, that.namespace);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, namespace);
    }
}
