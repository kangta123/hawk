package com.oc.hawk.message.domain.model;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

@Getter
public enum EventType {
    /**
     * 添加项目
     */
    PROJECT_ADD("添加项目", "%s，创建项目: %s"),
    /**
     * 发起构建项目
     */
    PROJECT_BUILD("构建项目", "%s, 执行构建项目"),
    /**
     * 构建失败
     */
    PROJECT_BUILD_FAILED("构建项目失败", "%s, 构建项目失败"),
    /**
     * 实例服务启动
     */
    RUNTIME_START("启动服务", "%s, 启动/更新实例: %s"),
    /**
     * 实例服务停止
     */
    RUNTIME_STOP("停止服务", "%s, 停止实例: %s"),
    /**
     * 添加服务实例配置
     */
    RUNTIME_ADD_CONFIG("添加服务配置", "%s, 添加实例配置信息: %s"),
    /**
     * 更新实例服务配置
     */
    RUNTIME_UPDATE_CONFIG("更新服务配置", "%s, 更新实例配置信息: %s"),
    /**
     * 删除实例服务配置
     */
    RUNTIME_DELETE_CONFIG("删除服务配置", "%s, 删除实例: %s"),
    /**
     * 构建成功后，自动部署事件
     */
    RUNTIME_AUTH_DEPLOYMENT_BY_PROJECT_BUILD("自动部署服务", "%s, 构建项目成功，自动部署实例: %s");

    private final String type;
    private final String title;

    EventType(String type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getTitle(String user, String... params) {
        String[] strings = ArrayUtils.addFirst(params, user);
        return String.format(title, (Object[]) strings);
    }
}
