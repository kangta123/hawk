package com.oc.hawk.base.domain.service;

public interface UserPasswordEncoder {
    String encodePassword(String password);

    boolean match(String rowPassword, String password);
}
