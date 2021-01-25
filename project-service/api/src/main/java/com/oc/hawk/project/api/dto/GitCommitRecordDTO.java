package com.oc.hawk.project.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GitCommitRecordDTO {

    /**
     * 作者
     */
    private String author;

    /**
     * 提交人
     */
    private String commitName;

    /**
     * 提交时间
     */
    private LocalDateTime commitTime;

    /**
     * 提交人邮箱
     */
    private String commitEmail;

    /**
     * 备注
     */
    private String message;

    /**
     * 版本号
     */
    private String versionNumber;


}
