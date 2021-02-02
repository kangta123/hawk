package com.oc.hawk.project.domain.facade;

import com.oc.hawk.project.domain.model.user.UserDepartment;
import com.oc.hawk.project.domain.model.user.UserInfo;

public interface UserFacade {
    UserInfo currentUser();

    UserDepartment currentUserDepartment();
}
