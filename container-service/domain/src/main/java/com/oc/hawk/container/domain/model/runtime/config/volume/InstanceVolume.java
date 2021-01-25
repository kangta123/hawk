package com.oc.hawk.container.domain.model.runtime.config.volume;

import com.google.common.base.Objects;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public abstract class InstanceVolume {
    private String mountPath;
    private String volumeName;

    public InstanceVolume(String volumeName, String mountPath) {
        this.volumeName = volumeName;
        this.mountPath = mountPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        InstanceVolume volume = (InstanceVolume) o;
        return Objects.equal(volumeName, volume.volumeName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(volumeName);
    }
}

