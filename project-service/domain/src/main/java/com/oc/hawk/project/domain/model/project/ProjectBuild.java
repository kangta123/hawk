package com.oc.hawk.project.domain.model.project;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;

@DomainValueObject
@Data
public class ProjectBuild {
    private BuildType type;
    private BuildCommand command;
    /**
     * 构建后文件输出路径，主要是npm构建使用
     */
    private String output;

    public ProjectBuild(BuildType buildType, BuildCommand buildCommand, String output) {
        this.type = buildType;
        this.command = buildCommand;
        this.output = output;
    }
}
