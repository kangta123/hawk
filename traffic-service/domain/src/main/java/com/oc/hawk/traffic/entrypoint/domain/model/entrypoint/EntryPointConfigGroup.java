package com.oc.hawk.traffic.entrypoint.domain.model.entrypoint;

import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@DomainEntity
@Getter
@Builder
public class EntryPointConfigGroup {

    private EntryPointGroupID groupId;
    private String groupName;
    private EntryPointGroupVisibility entryPointGroupVisibility;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof EntryPointConfigGroup)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        EntryPointConfigGroup group = (EntryPointConfigGroup) obj;
        if (group.getGroupId() == null) {
            return false;
        }
        return group.getGroupId().getId().equals(this.groupId.getId());
    }

}
