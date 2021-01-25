package com.oc.hawk.container.domain.model.build.protocal;

import lombok.Data;

@Data
public class ProtocolSubApp {
    private Long projectId;
    private String branch;
    private String appName;
    private String appPath;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProtocolSubApp{");
        sb.append("projectId=").append(projectId);
        sb.append(", branch='").append(branch).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", appPath='").append(appPath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
