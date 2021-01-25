package com.oc.hawk.container.runtime.port.driven.facade;

import com.oc.hawk.base.api.dto.QueryUserDTO;
import com.oc.hawk.base.api.dto.UserDTO;
import com.oc.hawk.container.domain.facade.UserFacade;
import com.oc.hawk.container.domain.model.runtime.UserInfo;
import com.oc.hawk.container.runtime.port.driven.facade.feign.BaseGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RemoteUserFacade implements UserFacade {
    private final BaseGateway baseGateway;

    @Override
    public List<UserInfo> getUserInfo(List<Long> userIds) {
        if (userIds == null) {
            return null;
        }

        QueryUserDTO queryUserDTO = new QueryUserDTO();
        queryUserDTO.setIds(userIds);
        List<UserDTO> users = baseGateway.queryUsers(queryUserDTO);
        if (users == null) {
            return null;
        }
        return users.stream().map(u -> new UserInfo(u.getName(), u.getId())).collect(Collectors.toList());
    }
}
