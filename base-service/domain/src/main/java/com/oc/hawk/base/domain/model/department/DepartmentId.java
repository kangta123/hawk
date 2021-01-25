package com.oc.hawk.base.domain.model.department;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;

@Getter
@DomainValueObject
public class DepartmentId {
    private Long id;

    private DepartmentId(Long id) {
        this.id = id;
    }

    public static DepartmentId create(Long departmentId) {
        return new DepartmentId(departmentId);
    }
}
