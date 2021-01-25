package com.oc.hawk.message.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDTO {

    private Long id;
    private String title;
    private String text;
    private String version;
    /**
     * 负责人
     */
    private String leaders;
    /**
     * 提出人
     */
    private String introducers;
    private LocalDateTime createTime;

}
