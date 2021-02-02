package com.oc.hawk.project.port.driven.facade;

import com.oc.hawk.api.constant.AccountHolder;
import com.oc.hawk.api.exception.BaseException;
import com.oc.hawk.base.api.dto.DepartmentDTO;
import com.oc.hawk.base.api.dto.UserDTO;
import com.oc.hawk.common.utils.AccountHolderUtils;
import com.oc.hawk.project.domain.facade.UserFacade;
import com.oc.hawk.project.domain.model.user.UserDepartment;
import com.oc.hawk.project.domain.model.user.UserInfo;
import com.oc.hawk.project.port.driven.facade.feign.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoteUserFacade implements UserFacade {
    private final UserGateway userGateway;

    @Override
    public UserInfo currentUser() {
        AccountHolder accountHolder = getCurrentAccountHolder();
        UserDTO userDTO = userGateway.getUserInfo(accountHolder.getId());
        DepartmentDTO department = userDTO.getDepartment();
        return new UserInfo(userDTO.getId(), userDTO.getName(), department.getId(), department.getMaster());
    }

    private AccountHolder getCurrentAccountHolder() {
        AccountHolder accountHolder = AccountHolderUtils.getAccountHolder();
        if (accountHolder == null) {
            throw new BaseException("需要认证的用户信息");
        }
        return accountHolder;
    }

    @Override
    public UserDepartment currentUserDepartment() {
        AccountHolder currentAccountHolder = getCurrentAccountHolder();
        DepartmentDTO userDepartment = userGateway.getUserDepartment(currentAccountHolder.getId());

        return new UserDepartment(userDepartment.getProjectName(), userDepartment.getName(), userDepartment.getId(), userDepartment.getMaster());
    }
}
