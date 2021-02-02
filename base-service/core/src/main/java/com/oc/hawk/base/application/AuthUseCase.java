package com.oc.hawk.base.application;

import com.oc.hawk.base.api.dto.AuthDTO;
import com.oc.hawk.base.api.dto.AuthIdentifier;
import com.oc.hawk.base.api.dto.PostAuthAction;
import com.oc.hawk.base.api.dto.RenewPasswordDTO;
import com.oc.hawk.base.application.representation.UserRepresentation;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserRepository;
import com.oc.hawk.base.domain.service.UserCredentialAuth;
import com.oc.hawk.base.domain.service.UserCredentialTokenGenerate;
import com.oc.hawk.base.domain.service.UserPasswordEncoder;
import com.oc.hawk.base.domain.service.UserRenewPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUseCase {
    private final UserRepository userRepository;
    private final UserRepresentation userRepresentation;
    private final UserPasswordEncoder passwordEncoder;

    public AuthDTO auth(AuthIdentifier authIdentifier) {
        log.info("auth user with key: {}, password: {}", authIdentifier.getKey(), authIdentifier.getPassword());
        final User user = new UserCredentialAuth(userRepository)
            .authenticate(authIdentifier.getKey(), authIdentifier.getPassword(), passwordEncoder);

        final AuthDTO authDTO = newAuthDTO(user);
        if (Objects.equals(authIdentifier.getPassword(), User.DEFAULT_PASSWORD)) {
            authDTO.setAction(PostAuthAction.RESET_PASSWORD);
        }
        return authDTO;
    }

    private AuthDTO newAuthDTO(User user) {
        String token = new UserCredentialTokenGenerate().generate(user);

        return userRepresentation.toAuthDto(user, token);
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthDTO renewPassword(RenewPasswordDTO renewPasswordDTO) {
        log.info("renew password with userId: {}", renewPasswordDTO.getUserId());

        String password = renewPasswordDTO.getPassword();
        String passwordCfm = renewPasswordDTO.getPasswordConfirm();
        final User user = new UserRenewPasswordService(userRepository, passwordEncoder)
            .renew(renewPasswordDTO.getUserId(), password, passwordCfm);
        return newAuthDTO(user);
    }
}
