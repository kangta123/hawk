package com.oc.hawk.traffic.application.entrypoint;

import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntryPointConfigGroupFactory {

    public EntryPointConfigGroup create(String groupName) {
        return EntryPointConfigGroup.builder()
            .groupName(groupName)
            .build();
    }

}
