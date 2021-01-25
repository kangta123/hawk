package com.oc.hawk.project.api.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProjectBuildStageCommand {
    private String stage;
    private boolean success;
    private String image;
}
