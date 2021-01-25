package com.oc.hawk.base.domain.model.user;

import com.oc.hawk.ddd.web.DomainPage;

import java.util.List;

public interface UserRepository {
    User getUser(UserId id);

    User getCurrent();

    void save(User user);

    boolean isEmailExisted(String email);

    boolean isAuthNameExisted(String authName);

    void deleteUser(UserId id);

    List<User> queryUser(String key, List<Long> ids, Boolean departmentIgnore);

    DomainPage<User> queryUserPage(int page, int size, String key);

    User byAuthName(String key);

    User byPhone(String key);

    User byEmail(String key);

    boolean isPhoneExisted(String phone);
}
