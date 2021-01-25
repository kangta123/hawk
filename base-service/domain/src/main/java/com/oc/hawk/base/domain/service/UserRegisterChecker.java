package com.oc.hawk.base.domain.service;

import com.oc.hawk.base.domain.model.department.DepartmentId;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserRepository;
import com.oc.hawk.base.domain.model.user.exception.UserRegisterInvalidParamException;
import com.oc.hawk.api.utils.NumberUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

@RequiredArgsConstructor
public class UserRegisterChecker {
    private final UserRepository userRepository;

    public void check(String name, String authName, String email, String phone, Long departmentId) {
        if (StringUtils.isEmpty(authName)) {
            throw new UserRegisterInvalidParamException("登录名不能为空");
        }
        if (StringUtils.isEmpty(name)) {
            throw new UserRegisterInvalidParamException("姓名不能为空");
        }
        if (StringUtils.isEmpty(email)) {
            throw new UserRegisterInvalidParamException("邮箱不能为空");
        }

        final User current = userRepository.getCurrent();

        DepartmentId deptId;
        if (current.isMaster()) {
            deptId = DepartmentId.create(departmentId);
        } else {
            deptId = current.getDepartmentId();
        }

        if (deptId == null) {
            throw new UserRegisterInvalidParamException("请选择部门");
        }

        if (userRepository.isEmailExisted(email)) {
            throw new UserRegisterInvalidParamException("邮箱：" + email + " 已存在！");
        }
        if (userRepository.isAuthNameExisted(authName)) {
            throw new UserRegisterInvalidParamException("登录名：" + authName + " 已存在！");
        }
        if (StringUtils.isNotEmpty(phone) && userRepository.isPhoneExisted(phone)) {
            throw new UserRegisterInvalidParamException("手机号：" + phone + " 已存在！");
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new UserRegisterInvalidParamException("邮箱不合法");
        }


        if (StringUtils.isNotEmpty(phone) && !NumberUtils.isMobile(phone)) {
            throw new UserRegisterInvalidParamException("手机号不合法");
        }

    }
}
