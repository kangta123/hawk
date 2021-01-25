package com.oc.hawk.base.port.driven.persistence.po;

import com.oc.hawk.base.domain.model.department.Department;
import com.oc.hawk.base.domain.model.department.DepartmentId;
import com.oc.hawk.base.domain.model.user.User;
import com.oc.hawk.base.domain.model.user.UserId;
import com.oc.hawk.common.hibernate.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@DynamicInsert()
@Table(name = "base_user_info")
public class UserPo extends BaseEntity {
    private String name;
    private String authName;

    private String phone;

    private String email;

    private String password;

    @ManyToOne
    private DepartmentPo department;

    public static UserPo createNew(User user) {
        final UserPo userPo = new UserPo();
        userPo.setAuthName(user.getAuthName());
        userPo.setEmail(user.getEmail());
        userPo.setName(user.getName());
        userPo.setPassword(user.getPassword());
        userPo.setPhone(user.getPhone());

        if (user.getId() != null) {
            userPo.setId(user.getId().getId());
        }

        return userPo;
    }

    public User toUser() {
        if(department == null){
            return null;
        }
        final Department department = Department.builder()
            .name(getDepartment().getName())
            .master(getDepartment().isMaster())
            .projectName(getDepartment().getProjectName())
            .id(DepartmentId.create(getDepartment().getId())).build();
        return User.builder().id(new UserId(getId())).phone(phone).name(name).authName(authName).password(password).email(email).department(department).build();
    }
}
