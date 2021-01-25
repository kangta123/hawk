package com.oc.hawk.base.api.dto;

import lombok.Data;

@Data
public class AuthIdentifier {
    private String password;
    private String key;
}
