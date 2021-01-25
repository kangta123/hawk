package com.oc.hawk.base.api.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String authName;
    private DepartmentDTO department;
}
