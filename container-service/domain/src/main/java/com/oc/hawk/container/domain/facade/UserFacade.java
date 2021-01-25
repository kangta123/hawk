package com.oc.hawk.container.domain.facade;

import com.oc.hawk.container.domain.model.runtime.UserInfo;

import java.util.List;

public interface UserFacade {
    List<UserInfo> getUserInfo(List<Long> userIds);
}
