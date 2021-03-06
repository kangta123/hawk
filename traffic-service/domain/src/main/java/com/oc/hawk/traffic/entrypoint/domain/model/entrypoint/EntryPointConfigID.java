package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class EntryPointConfigID {

    private Long id;

    public EntryPointConfigID(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EntryPointConfigID)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        EntryPointConfigID groupId = (EntryPointConfigID) obj;
        return groupId.getId().equals(this.getId());
    }
}
