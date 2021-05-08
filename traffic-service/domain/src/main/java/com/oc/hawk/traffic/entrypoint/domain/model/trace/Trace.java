package com.oc.hawk.traffic.entrypoint.domain.model.trace;

import com.oc.hawk.ddd.DomainEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
@DomainEntity
public class Trace {

    private final TraceId id;
    private final String host;
    private final HttpResource httpResource;
    
    private final Destination destination;
    private final String sourceAddr;
    
    private final Long timestamp;
    private final Latency latency;
    private final String requestId;
    private final String protocol;
    
    private final HttpRequestHeader requestHeaders;
    private final HttpResponseHeader responseHeaders;
    private final HttpResponseBody responseBody;
    private final HttpRequestBody requestBody;
    
    private final SpanContext spanContext;
    private final HttpResponseCode responseCode;
    private Long entryPointId;
    private String entryPointName;
    
    public void updateEntryPointIdAndName(Long entryPointId,String entryPointName) {
        if(Objects.nonNull(entryPointId)) {
            this.entryPointId = entryPointId;
        }
        if(Objects.nonNull(entryPointName)) {
            this.entryPointName = entryPointName;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trace trace = (Trace) o;
        return Objects.equals(requestId, trace.requestId) &&
                Objects.equals(spanContext, trace.spanContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, spanContext);
    }
}

