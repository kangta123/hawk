package com.oc.hawk.container.domain.model.runtime.config;

import com.oc.hawk.container.domain.model.runtime.UserInfo;
import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@DomainValueObject
@Getter
@NoArgsConstructor
public class InstanceManager {
    private String username;
    private Long userId;


    public InstanceManager(String username, Long userId) {
        this.username = username;
        this.userId = userId;
    }

    public static InstanceManager create(UserInfo userInfo) {
        InstanceManager instanceManager = new InstanceManager();
        instanceManager.userId = userInfo.getId();
        instanceManager.username = userInfo.getName();
        return instanceManager;
    }

    public static List<InstanceManager> create(List<UserInfo> users) {
        if (users == null) {
            return null;
        }
        return users.stream().map(InstanceManager::create).collect(Collectors.toList());
    }
}
