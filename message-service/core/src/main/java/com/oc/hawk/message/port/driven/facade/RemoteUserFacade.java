package com.oc.hawk.message.port.driven.facade;

import com.oc.hawk.base.api.dto.QueryUserDTO;
import com.oc.hawk.base.api.dto.UserDTO;
import com.oc.hawk.message.domain.facade.UserInfoFacade;
import com.oc.hawk.message.domain.model.UserInfo;
import com.oc.hawk.message.port.driven.facade.feign.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RemoteUserFacade implements UserInfoFacade {
    private final UserGateway userGateway;

    @Override
    public UserInfo getUserInfo(long userId) {
        return userGateway.getUser(userId);
    }

    @Override
    public Map<Long, String> getUserName(List<Long> ids) {
        QueryUserDTO queryUserDTO = new QueryUserDTO();
        queryUserDTO.setIds(ids);
        List<UserDTO> userInfo = userGateway.getUserInfo(queryUserDTO);
        if (userInfo != null) {
            return userInfo.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        }
        return null;
    }
}
