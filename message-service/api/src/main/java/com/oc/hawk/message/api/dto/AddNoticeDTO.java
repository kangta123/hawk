package com.oc.hawk.message.api.dto;

import lombok.Data;

@Data
public class AddNoticeDTO {

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
    private Long creator;
    private String creatorName;

}
