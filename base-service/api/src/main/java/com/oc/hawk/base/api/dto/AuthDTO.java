package com.oc.hawk.base.api.dto;

import lombok.Data;

@Data
public class AuthDTO {
    private PostAuthAction action;
    private String error;
    private String name;
    private String token;
    private Long id;
    private String email;
    private DepartmentDTO department;
}
