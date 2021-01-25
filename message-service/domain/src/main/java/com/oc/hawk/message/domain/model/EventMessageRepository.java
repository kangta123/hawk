package com.oc.hawk.message.domain.model;

import com.oc.hawk.ddd.web.DomainPage;

public interface EventMessageRepository {
    DomainPage<EventMessage> byProjectId(long projectId, int page, int size);

    void save(EventMessage eventMessage);

    DomainPage<EventMessage> byInstanceId(Long instanceId, int page, int size);
}
