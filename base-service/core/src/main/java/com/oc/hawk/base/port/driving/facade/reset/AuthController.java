package com.oc.hawk.base.port.driving.facade.reset;

import com.oc.hawk.base.api.dto.AuthDTO;
import com.oc.hawk.base.api.dto.AuthIdentifier;
import com.oc.hawk.base.api.dto.RenewPasswordDTO;
import com.oc.hawk.base.application.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUseCase authUseCase;

    @PostMapping
    public AuthDTO auth(@RequestBody AuthIdentifier authIdentifier) {
        return authUseCase.auth(authIdentifier);
    }

    @PutMapping("/password")
    public AuthDTO renewPassword(@RequestBody RenewPasswordDTO renewPasswordDTO) {
        return authUseCase.renewPassword(renewPasswordDTO);
    }

}
