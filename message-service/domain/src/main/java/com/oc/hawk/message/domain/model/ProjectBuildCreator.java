package com.oc.hawk.message.domain.model;

import lombok.Getter;

@Getter
public class ProjectBuildCreator {
    private Long userId;
    private String userName;

    public ProjectBuildCreator(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
