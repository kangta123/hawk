package com.oc.hawk.ddd.event;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;


@Getter
@DomainValueObject
@NoArgsConstructor
public final class DomainEvent {
    private LocalDateTime createdAt;
    private Object data;
    private String type;
    private Long domainId;
    private String id;
    private String clz;

    private Long userId;

    public static DomainEvent byData(String type, Object data) {
        return byData(null, type, data);
    }

    public static DomainEvent byData(Long domainId, String type, Object data) {
        DomainEvent event = byType(domainId, type);
        event.data = data;
        event.clz = data.getClass().getCanonicalName();
        return event;
    }

    public static DomainEvent byType(Long domainId, String type) {
        DomainEvent domainEvent = new DomainEvent();
        domainEvent.createdAt = LocalDateTime.now();
        domainEvent.type = type;
        domainEvent.domainId = domainId;
        return domainEvent;

    }

    public DomainEvent withUser(Long userId) {
        this.userId = userId;
        return this;
    }

    public DomainEvent withId(String id) {
        this.id = id;
        return this;
    }

    public boolean is(String... types) {
        for (String type : types) {
            if (StringUtils.equals(type, this.type)) {
                return true;
            }
        }
        return false;
    }

    public void resetData(Object o) {
        this.data = o;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DomainEvent{");
        sb.append("createdAt=").append(createdAt);
        sb.append(", type='").append(type).append('\'');
        sb.append(", domainId=").append(domainId);
        sb.append(", id='").append(id).append('\'');
        sb.append(", clz='").append(clz).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
