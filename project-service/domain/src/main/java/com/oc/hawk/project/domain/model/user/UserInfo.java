package com.oc.hawk.project.domain.model.user;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@DomainValueObject
public class UserInfo {
    private Long userId;
    private String userName;
    private Long departmentId;
    private boolean isMaster;
}
