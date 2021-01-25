package com.oc.hawk.container.api.dto;

import com.oc.hawk.container.api.command.CreateInstanceConfigCommand;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddServiceAppDTO {
    private CreateInstanceConfigCommand configuration;
    private String app;
    private Integer scale;
    private Long projectId;
    private String name;
    private String descn;
    private Long appId;

}
