package com.oc.hawk.base.application;

import com.oc.hawk.base.api.dto.AddUserDTO;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserRepository;
import com.oc.hawk.base.domain.service.UserPasswordEncoder;
import com.oc.hawk.base.domain.service.UserRegisterChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFactory {
    private final UserRepository userRepository;
    private final UserPasswordEncoder userPasswordEncoder;

    public User createNewUser(AddUserDTO addUserDTO) {
        new UserRegisterChecker(userRepository).check(addUserDTO.getName(), addUserDTO.getAuthName(), addUserDTO.getEmail(), addUserDTO.getPhone(), addUserDTO.getDepartmentId());

        final User current = userRepository.getCurrent();

        final User user = User.builder()
            .authName(addUserDTO.getAuthName())
            .name(addUserDTO.getName())
            .email(addUserDTO.getEmail())
            .department(current.getDepartment())
            .phone(addUserDTO.getPhone()).build();


        user.applyNewPassword(User.DEFAULT_PASSWORD, userPasswordEncoder);
        return user;
    }
}
