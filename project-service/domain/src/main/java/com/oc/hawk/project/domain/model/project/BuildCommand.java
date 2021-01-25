package com.oc.hawk.project.domain.model.project;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@DomainValueObject
public class BuildCommand {
    private String command;

    public BuildCommand(String command) {
        this.command = command;
    }

    public static BuildCommand createOrDefault(String buildCommand, String command) {
        return new BuildCommand(StringUtils.isNotEmpty(buildCommand) ? buildCommand : command);
    }

}
