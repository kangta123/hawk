package com.oc.hawk.message.port.driven.persistence.po;

import com.oc.hawk.common.hibernate.BaseEntity;
import com.oc.hawk.message.domain.model.EventMessage;
import com.oc.hawk.message.domain.model.EventType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "message_event")
@Entity
public class EventMessagePO extends BaseEntity {
    private Long userId;
    private Long departmentId;
    private Long projectId;
    private Long instanceId;
    private String type;
    private String title;
    private LocalDateTime time;
    private String message;

    public static EventMessagePO create(EventMessage eventMessage) {
        EventMessagePO po = new EventMessagePO();
        po.setDepartmentId(eventMessage.getDepartmentId());
        po.setMessage(eventMessage.getMessage());
        po.setProjectId(eventMessage.getProject());
        po.setTime(eventMessage.getTime());
        po.setTitle(eventMessage.getTitle());
        po.setType(eventMessage.getType().name());
        po.setUserId(eventMessage.getUserId());
        po.setInstanceId(eventMessage.getInstance());
        return po;
    }


    public EventMessage createEventMessage() {
        return EventMessage.builder()
            .departmentId(getDepartmentId())
            .message(getMessage())
            .project(getProjectId())
            .userId(getUserId())
            .title(getTitle())
            .type(EventType.valueOf(getType()))
            .time(getTime())
            .instance(getInstanceId())
            .build();
    }
}
