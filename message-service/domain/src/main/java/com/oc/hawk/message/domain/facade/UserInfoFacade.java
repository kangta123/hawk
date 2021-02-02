package com.oc.hawk.message.domain.facade;

import com.oc.hawk.message.domain.model.UserInfo;

import java.util.List;
import java.util.Map;

public interface UserInfoFacade {
    Map<Long, String> getUserName(List<Long> ids);

    UserInfo getUserInfo(long userId);
}
