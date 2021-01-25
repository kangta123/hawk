package com.oc.hawk.base.domain.model.user;

import lombok.Getter;

@Getter
public class UserId {
    private long id;

    public UserId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
