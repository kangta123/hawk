package com.oc.hawk.project.domain.model.buildjob;

import lombok.Getter;

@Getter
public enum JobStage {
    Created("创建任务"), Ready("准备完毕"), Start("构建开始"), GitClone("克隆代码库"), Build("构建项目"), DockerBuild("打包"), DockerPush("推送数据"), End("构建结束");
    String title;

    JobStage(String title) {
        this.title = title;
    }
}
