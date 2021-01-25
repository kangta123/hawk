package com.oc.hawk.base.application;

import com.oc.hawk.base.domain.service.UserPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SpringSecUserPasswordEncoder implements UserPasswordEncoder {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean match(String rawPassword, String password) {
        return BCrypt.checkpw(rawPassword, password);
    }

}
