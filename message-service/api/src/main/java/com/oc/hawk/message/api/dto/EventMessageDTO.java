package com.oc.hawk.message.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventMessageDTO {
    private String userName;
    private String type;
    private String typeDesc;
    private LocalDateTime time;
    private String title;
}
