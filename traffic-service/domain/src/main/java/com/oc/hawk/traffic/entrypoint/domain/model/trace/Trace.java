package com.oc.hawk.traffic.entrypoint.domain.model.trace;

import java.util.Map;
import java.util.Objects;

import com.oc.hawk.ddd.DomainEntity;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.Destination;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpRequestBody;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpRequestHeader;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResponseBody;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResponseCode;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResponseHeader;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.Latency;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.SpanContext;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@DomainEntity
public class Trace {

    private final TraceId id;
    private String host;
    private HttpResource httpResource;
    
    private Destination destination;
    private String sourceAddr;
    
    private Long timestamp;
    private Latency latency;
    private String requestId;
    private String protocol;
    
    private HttpRequestHeader requestHeaders;
    private HttpResponseHeader responseHeaders;
    private HttpResponseBody responseBody;
    private HttpRequestBody requestBody;
    
    private SpanContext spanContext;
    private HttpResponseCode responseCode;
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
    
}

