package com.oc.hawk.base.domain.model.department;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Department {
    private DepartmentId id;
    private String name;
    private String projectName;
    private boolean master;
}
