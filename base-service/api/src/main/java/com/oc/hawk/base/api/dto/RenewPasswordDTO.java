package com.oc.hawk.base.api.dto;

import lombok.Data;

@Data
public class RenewPasswordDTO {
    private Long userId;
    private String password;
    private String passwordConfirm;
}
