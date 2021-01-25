package com.oc.hawk.project.domain.model.user;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@DomainValueObject
@AllArgsConstructor
public class UserDepartment {
    private String groupName;
    private String name;
    private long id;
    private boolean isMaster;
}
