package com.oc.hawk.container.domain.service;

import com.oc.hawk.container.domain.model.runtime.config.InstanceConfigRepository;
import com.oc.hawk.container.domain.model.runtime.config.InstanceId;
import com.oc.hawk.container.domain.model.runtime.config.InstanceName;
import com.oc.hawk.ddd.DomainService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class InstanceCreateChecker {

    private final InstanceConfigRepository instanceConfigRepository;

    public boolean isDuplicate(String namespace, InstanceName name, InstanceId instanceId) {
        InstanceId existedInstanceId = instanceConfigRepository.existed(namespace, name);
        if (existedInstanceId == null) {
            return false;
        } else {
            if (instanceId == null) {
                return true;
            } else {
                return instanceId != existedInstanceId;
            }
        }


    }
}
