package com.oc.hawk.base.api.dto;

import lombok.Data;


@Data
public class AddUserDTO {
    private String name;
    private String authName;
    private String phone;
    private String email;
    private Long departmentId;

}
