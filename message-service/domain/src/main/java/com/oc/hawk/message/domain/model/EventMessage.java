package com.oc.hawk.message.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventMessage {
    private Long userId;
    private Long departmentId;
    private Long project;
    private Long instance;
    private EventType type;
    private String title;
    private LocalDateTime time;
    private String message;

}
