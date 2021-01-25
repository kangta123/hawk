package com.oc.hawk.message.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfo {
    private Long id;
    private String name;
    private long departmentId;

    public UserInfo(Long id, String name, long departmentId) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
    }
}
