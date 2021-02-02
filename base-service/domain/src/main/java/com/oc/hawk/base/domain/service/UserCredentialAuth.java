package com.oc.hawk.base.domain.service;

import com.oc.hawk.api.utils.NumberUtils;
import com.oc.hawk.base.domain.model.auth.exception.InvalidUsernameOrPasswordException;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.EmailValidator;

@RequiredArgsConstructor
public class UserCredentialAuth {
    private final UserRepository userRepository;

    public User authenticate(String key, String password, UserPasswordEncoder passwordEncoder) {
        User user;
        if (EmailValidator.getInstance().isValid(key)) {
            user = userRepository.byEmail(key);
        } else if (NumberUtils.isMobile(key)) {
            user = userRepository.byPhone(key);
        } else {
            user = userRepository.byAuthName(key);
        }

        if (user == null) {
            throw new InvalidUsernameOrPasswordException("用户不存在");
        }
        if (!passwordEncoder.match(password, user.getPassword())) {
            throw new InvalidUsernameOrPasswordException("密码错误");
        }
        return user;
    }

}
