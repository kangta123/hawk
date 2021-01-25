package com.oc.hawk.base.domain.service;

import com.oc.hawk.api.exception.AppBusinessException;
import com.oc.hawk.api.exception.DomainNotFoundException;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserId;
import com.oc.hawk.base.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@RequiredArgsConstructor
public class UserRenewPasswordService {
    private final UserRepository userRepository;
    private final UserPasswordEncoder passwordEncoder;
    private final int MIN_PASSWORD_LENGTH = 5;

    public User renew(Long userId, String password, String passwordConfirm) {
        boolean empty = StringUtils.isEmpty(password) || StringUtils.isEmpty(passwordConfirm);

        boolean passwordLengthValidation = password.length() < MIN_PASSWORD_LENGTH;

        boolean passwordEqual = !Objects.equals(password, passwordConfirm);
        if (empty) {
            throw new AppBusinessException("密码不能为空");
        }
        if (passwordLengthValidation) {
            throw new AppBusinessException("密码长度大于" + MIN_PASSWORD_LENGTH + "个字符");
        }
        if (passwordEqual) {
            throw new AppBusinessException("两次密码不符");
        }

        User user;
        if (userId != null) {
            user = userRepository.getUser(new UserId(userId));
        } else {
            user = userRepository.getCurrent();
        }

        if (user == null) {
            throw new DomainNotFoundException();
        }

        user.applyNewPassword(password, passwordEncoder);

        userRepository.save(user);

        return user;

    }
}
