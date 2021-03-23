package com.oc.hawk.traffic.entrypoint.domain.model.trace;

import com.oc.hawk.ddd.DomainValueObject;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DomainValueObject
@Getter
@NoArgsConstructor
public class TraceId {

    public Long id;

    public TraceId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}
