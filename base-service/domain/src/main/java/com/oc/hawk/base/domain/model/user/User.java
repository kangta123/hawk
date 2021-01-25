package com.oc.hawk.base.domain.model.user;

import com.oc.hawk.base.domain.model.department.Department;
import com.oc.hawk.base.domain.model.department.DepartmentId;
import com.oc.hawk.base.domain.service.UserPasswordEncoder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {
    public final static String DefaultPassword = "password123";
    private final String name;
    private final String authName;
    private final String phone;
    private final String email;
    private final Department department;
    private final UserId id;
    private String password;

    public boolean isMaster() {
        return department.isMaster();
    }

    public DepartmentId getDepartmentId() {
        return department.getId();
    }

    public void applyNewPassword(String newPassword, UserPasswordEncoder userPasswordEncoder) {
        this.password = userPasswordEncoder.encodePassword(newPassword);
    }

}
