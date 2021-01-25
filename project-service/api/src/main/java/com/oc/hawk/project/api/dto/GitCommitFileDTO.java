package com.oc.hawk.project.api.dto;

import lombok.Data;

@Data
public class GitCommitFileDTO {

    /**
     * 操作类型
     */
    private String changeType;

    /**
     * 操作记录
     */
    private String filePath;


}
