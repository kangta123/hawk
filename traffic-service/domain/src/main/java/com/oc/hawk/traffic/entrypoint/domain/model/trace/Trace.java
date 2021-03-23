package com.oc.hawk.traffic.entrypoint.domain.model.trace;

import com.oc.hawk.ddd.DomainEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@DomainEntity
public class Trace {

    private final TraceId historyId;
    private final Long start;
}

