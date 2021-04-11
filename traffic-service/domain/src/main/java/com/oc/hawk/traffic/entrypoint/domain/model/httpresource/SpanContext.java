package com.oc.hawk.traffic.entrypoint.domain.model.httpresource;

import lombok.Getter;

@Getter
public class SpanContext {
    
    private String spanId;
    
    private String parentSpanId;
    
    private String traceId;
    
    public SpanContext(String spanId,String parentSpanId,String traceId) {
        this.spanId=spanId;
        this.parentSpanId=parentSpanId;
        this.traceId=traceId;
    }
    
}
